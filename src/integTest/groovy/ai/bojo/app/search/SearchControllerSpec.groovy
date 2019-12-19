package ai.bojo.app.search


import ai.bojo.app.BaseSpecification
import ai.bojo.app.Url
import org.springframework.http.HttpHeaders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class SearchControllerSpec extends BaseSpecification {

    def 'should find "PageModel" by query'() {
        given:
        def pageNumber = 0
        def query = 'Elvis'

        and:
        def req = get("${Url.SEARCH_QUOTE}?query=${query}&page=${pageNumber}")
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

        and:
        res.andExpect(jsonPath('$._embedded.quotes').hasJsonPath())
        res.andExpect(jsonPath('$._links.first.href').hasJsonPath())
        res.andExpect(jsonPath('$._links.last.href').hasJsonPath())
        res.andExpect(jsonPath('$._links.next.href').hasJsonPath())
        res.andExpect(jsonPath('$._links.prev.href').hasJsonPath())
        res.andExpect(jsonPath('$._links.self.href').hasJsonPath())
        res.andExpect(jsonPath('$.count').hasJsonPath())
        res.andExpect(jsonPath('$.total').hasJsonPath())

        where:
        acceptHeader << [
                'application/hal+json',
                'application/json'
        ]
    }
}
