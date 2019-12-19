package ai.bojo.app.tag

import ai.bojo.app.BaseSpecification
import ai.bojo.app.Url
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class TagControllerSpec extends BaseSpecification {

    void 'should create a new TagEntity'() {
        given:
        def entity = new TagEntity(value: 'Jeremy Corbyn')

        and:
        def req = post(Url.TAG)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, httpBasic())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ORIGIN, '*')
                .content(serialize(entity))

        when:
        def res = mvc.perform(req)

        then:
        res.andExpect(status().isCreated())

        and: 'headers are correct'
        res.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, 'true'))
        res.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, '*'))
        res.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))

        and: 'body has a tag'
        res.andExpect(jsonPath('$.created_at').hasJsonPath())
        res.andExpect(jsonPath('$.tag_id').hasJsonPath())
        res.andExpect(jsonPath('$.updated_at').hasJsonPath())
        res.andExpect(jsonPath('$.value').value('Jeremy Corbyn'))
    }

    void 'should find a "TagEntity" by id'() {
        given:
        def id = 'c1dIgMTURW-LllIZWSBESa'
        def req = get("${Url.TAG}/${id}")
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

        and: 'body has a tag'
        res.andExpect(jsonPath('$.created_at').hasJsonPath())
        res.andExpect(jsonPath('$.tag_id').value(id))
        res.andExpect(jsonPath('$.updated_at').hasJsonPath())
        res.andExpect(jsonPath('$.value').value('Music'))

        where:
        acceptHeader << [
                'application/hal+json',
                'application/json'
        ]
    }

    void 'should report an error if "TagEntity" does not exist'() {
        given:
        def id = 'does-not-exist'
        def req = get("${Url.QUOTE}/${id}")
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
