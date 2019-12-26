import com.moowork.gradle.node.npm.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.energizedwork.webdriver-binaries") version "1.4"
    id("com.github.node-gradle.node") version "2.2.0"
    id("com.palantir.docker") version "0.22.1"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.gradle.groovy")
    id("org.springframework.boot") version "2.2.1.RELEASE"

    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
}

val appName = "app"
val appVer by lazy { gitRev() }

group = "ai.bojo"
version = appVer
java.sourceCompatibility = JavaVersion.VERSION_1_8

// versions
val chromeDriverVersion = "2.36"
val geckoDriverVersion = "0.24.0"
val seleniumVersion = "3.141.59"
val spockVersion = "1.3-groovy-2.5"

val sourceSets = the<SourceSetContainer>()

sourceSets {
    create("browserTest") {
        java.srcDir(file("src/browserTest/groovy"))
        resources.srcDir(file("src/browserTest/resources"))
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
    create("integTest") {
        java.srcDir(file("src/integTest/groovy"))
        resources.srcDir(file("src/integTest/resources"))
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.postgresql:postgresql:42.2.8")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.hateoas:spring-hateoas:1.0.1.RELEASE")
    implementation("org.springdoc:springdoc-openapi-core:1.1.49")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.gebish:geb-spock:3.2")
    testImplementation("org.seleniumhq.selenium:selenium-chrome-driver:${seleniumVersion}")
    testImplementation("org.seleniumhq.selenium:selenium-firefox-driver:${seleniumVersion}")
    testImplementation("org.seleniumhq.selenium:selenium-java:${seleniumVersion}")
    testImplementation("org.spockframework:spock-core:${spockVersion}") { exclude(group = "org.codehaus.groovy") }
    testImplementation("org.spockframework:spock-spring:${spockVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test") { exclude(group = "org.junit.vintage", module = "junit-vintage-engine") }
}

webdriverBinaries {
    chromedriver = chromeDriverVersion
    geckodriver = geckoDriverVersion
}

springBoot {
    buildInfo {
        properties {
            artifact = "$appName-$appVer.jar"
            version = appVer
            name = appName
        }
    }
}

tasks {
    bootJar {
        manifest {
            attributes("Multi-Release" to true)
        }

        archiveBaseName.set(appName)
        archiveVersion.set(appVer)

        if (project.hasProperty("archiveName")) {
            archiveFileName.set(project.properties["archiveName"] as String)
        }
    }

    clean {
        delete(file("$projectDir/src/main/node/node_modules"))
    }

    docker {
        val bootJar = bootJar.get()
        val imageName = "$group/$appName"

        name = "$imageName:latest"
        tag("current", "$imageName:$appVer")
        tag("latest", "$imageName:latest")
        tag("herokuProduction", "registry.heroku.com/bojo-prod/web")
        tag("herokuStaging", "registry.heroku.com/bojo-stage/web")

        files(bootJar.archiveFile)
        files(file("$projectDir/src/main/resources"))

        setDockerfile(file("$projectDir/src/main/docker/Dockerfile"))
        buildArgs(
                mapOf("JAR_FILE" to bootJar.archiveFileName.get())
        )
    }

    node {
        // Version of node to use.
        version = "12.14.0"

        // Version of npm to use.
        npmVersion = "6.13.4"

        // If true, it will download node using above parameters.
        // If false, it will try to use globally installed node.
        download = true

        nodeModulesDir = file("$projectDir/src/main/node/node_modules")
    }

    processResources {
        dependsOn("processNodeResources")
    }

    register<Copy>("processNodeResources") {
        description = "Processes node resources."
        group = "other"

        from("$projectDir/src/main/node/.dist/css/")
        into("$buildDir/resources/main/static/css/")

        dependsOn("buildNode")
    }

    register<NpmTask>("buildNode") {
        group = "build"

        dependsOn("npm_install")
        setArgs(listOf("run", "build"))
    }

    register<Test>("browserTest") {
        description = "Runs the browser tests in headless mode."
        group = "verification"
        testClassesDirs = sourceSets["browserTest"].output.classesDirs
        classpath = sourceSets["browserTest"].runtimeClasspath

        outputs.upToDateWhen { false }

        systemProperty("geb.build.reportsDir", reporting.file("geb/headless"))
        systemProperty("geb.env", "headless")
    }

    register<Test>("integrationTest") {
        description = "Runs the integration tests."
        group = "verification"
        testClassesDirs = sourceSets["integTest"].output.classesDirs
        classpath = sourceSets["integTest"].runtimeClasspath
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}

fun gitRev() = ProcessBuilder("git", "rev-parse", "HEAD").start().let { p ->
    p.waitFor(100, TimeUnit.MILLISECONDS)
    p.inputStream.bufferedReader().readLine() ?: "none"
}
