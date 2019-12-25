package ai.bojo.app.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
                .components(Components())
                .info(Info()
                        .contact(Contact()
                                .email("m@matchilling.com")
                                .name("Mathias Schilling")
                        )
                        .description("Api & web archive for the dumbest things Boris Johnson has ever said.")
                        .license(License()
                                .name("GNU General Public License v3.0")
                                .url("https://github.com/bojo-ai/bojo-app/blob/master/LICENSE.md")
                        )
                        .title("BoJo AI")
                        .version("1.0.0")
                )
                .servers(listOf(
                        Server()
                                .url("https://www.bojo.ai")
                                .description("Production server (uses live data)")
                ))
    }
}