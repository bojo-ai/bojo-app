package ai.bojo.app.author

import ai.bojo.app.BaseSpecification
import ai.bojo.app.Url
import org.springframework.http.HttpHeaders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class AuthorControllerSpec extends BaseSpecification {

    void 'should find an author by id'() {
        given:
        def id = 'wVE8Y7BoRKCBkxs1JkqBvw'
        def req = get("${Url.AUTHOR}/${id}")
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

        and: 'body has an author'
        res.andExpect(jsonPath('$._links.self.href').value('http://localhost/author/wVE8Y7BoRKCBkxs1JkqBvw'))
        res.andExpect(jsonPath('$.author_id').value('wVE8Y7BoRKCBkxs1JkqBvw'))
        res.andExpect(jsonPath('$.bio').value(null))
        res.andExpect(jsonPath('$.created_at').hasJsonPath())
        res.andExpect(jsonPath('$.name').value('Boris Johnson'))
        res.andExpect(jsonPath('$.slug').value('boris-johnson'))
        res.andExpect(jsonPath('$.updated_at').hasJsonPath())

        where:
        acceptHeader << [
                'application/hal+json',
                'application/json'
        ]
    }

    void 'should report an error if "Author" does not exist'() {
        given:
        def id = 'does-not-exist'
        def req = get("${Url.AUTHOR}/${id}")
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
