package ai.bojo.app.configuration

import ai.bojo.app.Url
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class SecurityConfig
(
        @Value("\${spring.security.user.name}")
        private val username: String,

        @Value("\${spring.security.user.password}")
        private val password: String
) : WebSecurityConfigurerAdapter(true) {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth
                ?.inMemoryAuthentication()
                ?.withUser(username)
                ?.password("{noop}${password}")
                ?.roles("USER")
    }

    override fun configure(http: HttpSecurity?) {
        http
                ?.authorizeRequests()
                ?.antMatchers(HttpMethod.POST, Url.TAG)
                ?.hasRole("USER")
                ?.and()
                ?.httpBasic()
    }
}