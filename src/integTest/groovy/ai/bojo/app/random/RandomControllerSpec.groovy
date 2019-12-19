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
        res.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, 'true'))
        res.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, '*'))
        res.andExpect(header().string(HttpHeaders.CONTENT_TYPE, acceptHeader))

        and: 'body has a quote'
        res.andExpect(jsonPath('$.appeared_at').hasJsonPath())
        res.andExpect(jsonPath('$.created_at').hasJsonPath())
        res.andExpect(jsonPath('$.quote_id').hasJsonPath())
        res.andExpect(jsonPath('$.tags').hasJsonPath())
        res.andExpect(jsonPath('$.updated_at').hasJsonPath())
        res.andExpect(jsonPath('$.value').hasJsonPath())

        where:
        acceptHeader << [
                'application/hal+json',
                'application/json'
        ]
    }
}
