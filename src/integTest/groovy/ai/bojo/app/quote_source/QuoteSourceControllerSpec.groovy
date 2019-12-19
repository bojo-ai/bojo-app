package ai.bojo.app.quote_source

import ai.bojo.app.BaseSpecification
import ai.bojo.app.Url
import org.springframework.http.HttpHeaders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class QuoteSourceControllerSpec extends BaseSpecification {

    void 'should find a "QuoteSourceEntity" by id'() {
        given:
        def id = 'in8rU9GMRxeeweuLiqz9yg'
        def req = get("${Url.QUOTE_SOURCE}/${id}")
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

        and: 'body has a quote source'
        res.andExpect(jsonPath('$._links.self.href').hasJsonPath())
        res.andExpect(jsonPath('$.created_at').hasJsonPath())
        res.andExpect(jsonPath('$.filename').value(null))
        res.andExpect(jsonPath('$.quote_source_id').hasJsonPath())
        res.andExpect(jsonPath('$.remarks').value('The Big Book of Boris'))
        res.andExpect(jsonPath('$.updated_at').hasJsonPath())
        res.andExpect(jsonPath('$.url').value('https://www.dailymail.co.uk'))

        where:
        acceptHeader << [
                'application/hal+json',
                'application/json'
        ]
    }

    void 'should report an error if "QuoteSourceEntity" does not exist'() {
        given:
        def id = 'does-not-exist'
        def req = get("${Url.QUOTE_SOURCE}/${id}")
                .accept(acceptHeader)
                .header('Origin', '*')

        expect:
        mvc.perform(req).andExpect(status().isNotFound())

        where:
        acceptHeader << [
                'application/hal+json',
                'application/json'
        ]
    }
}
