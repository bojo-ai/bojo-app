package ai.bojo.app.random

import ai.bojo.app.BaseSpecification
import ai.bojo.app.Url
import org.springframework.http.HttpHeaders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class RandomControllerSpec extends BaseSpecification {

    void 'should return a random "QuoteEntity"'() {
        given:
        def req = get("${Url.RANDOM}/quote")
                .accept(acceptHeader)
                .header('Origin', '*')

        when:
        def res = mvc.perform(req)

        then:
        res.andExpect(status().isOk())

        and: 'headers are correct'
        res.andExpect {
            header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, 'true')
            header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, '*')
            header().string(HttpHeaders.CONTENT_TYPE, acceptHeader)
        }

        and: 'body has a quote'
        res.andExpect {
            jsonPath('$.appeared_at').hasJsonPath()
            jsonPath('$.created_at').hasJsonPath()
            jsonPath('$.quote_id').hasJsonPath()
            jsonPath('$.tags').hasJsonPath()
            jsonPath('$.updated_at').hasJsonPath()
            jsonPath('$.value').hasJsonPath()
        }

        where:
        acceptHeader << [
                'application/hal+json',
                'application/json'
        ]
    }
}
