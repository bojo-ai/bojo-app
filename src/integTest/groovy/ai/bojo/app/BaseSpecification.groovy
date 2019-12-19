package ai.bojo.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.text.SimpleDateFormat

@ActiveProfiles("h2")
@AutoConfigureMockMvc
@SpringBootTest
abstract class BaseSpecification extends Specification {

    @Autowired
    MockMvc mvc

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    ObjectMapper objectMapper = new ObjectMapper()
            .setDateFormat(simpleDateFormat)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

    @Value('${spring.security.user.name}')
    String username

    @Value('${spring.security.user.password}')
    String password

    def httpBasic() {
        def secret = "${username}:${password}"
                .bytes
                .encodeBase64()
                .toString()

        return "Basic ${secret}"
    }

    def serialize(Object object) {
        return objectMapper.writeValueAsString(object)
    }
}