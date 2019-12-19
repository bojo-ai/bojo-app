package ai.bojo.app.quote

import ai.bojo.app.BaseSpecification
import ai.bojo.app.Url
import ai.bojo.app.author.AuthorEntity
import ai.bojo.app.quote_source.QuoteSourceEntity
import ai.bojo.app.tag.TagEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

import java.sql.Timestamp

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class QuoteControllerSpec extends BaseSpecification {

    void 'should create a new QuoteEntity'() {
        given:
        def appearedAt = new Timestamp(new Date().time)
        def author = new AuthorEntity(authorId: 'wVE8Y7BoRKCBkxs1JkqBvw')
        def source = new QuoteSourceEntity(url: 'https://www.telegraph.co.uk/news/2016/05/14/boris-johnson-the-eu-wants-a-superstate-just-as-hitler-did/')
        def tag = new TagEntity(value: 'Napoleon')
        def value = 'Napoleon, Hitler, various people tried this out, and it ends tragically. The EU is an attempt to do this by different methods.'
        def entity = new QuoteEntity(author: author, appearedAt: appearedAt, source: source, tags: [tag], value: value)

        and:
        def req = post(Url.QUOTE)
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

        and: 'body has a quote'
        res.andExpect(jsonPath('$.appeared_at').hasJsonPath())
        res.andExpect(jsonPath('$.created_at').hasJsonPath())
        res.andExpect(jsonPath('$.quote_id').hasJsonPath())
        res.andExpect(jsonPath('$.tags').value(['Napoleon']))
        res.andExpect(jsonPath('$.updated_at').hasJsonPath())
        res.andExpect(jsonPath('$.value').value(value))

        and: 'body has an author embedded'
        res.andExpect(jsonPath('$._embedded.authors[0]._links.self.href').value('http://localhost/author/wVE8Y7BoRKCBkxs1JkqBvw'))
        res.andExpect(jsonPath('$._embedded.authors[0].author_id').value('wVE8Y7BoRKCBkxs1JkqBvw'))
        res.andExpect(jsonPath('$._embedded.authors[0].bio').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.authors[0].created_at').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.authors[0].name').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.authors[0].slug').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.authors[0].updated_at').hasJsonPath())

        and: 'body has a source embedded'
        res.andExpect(jsonPath('$._embedded.sources[0]._links.self.href').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.sources[0].created_at').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.sources[0].filename').value(null))
        res.andExpect(jsonPath('$._embedded.sources[0].quote_source_id').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.sources[0].remarks').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.sources[0].updated_at').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.sources[0].url').value('https://www.telegraph.co.uk/news/2016/05/14/boris-johnson-the-eu-wants-a-superstate-just-as-hitler-did/'))
    }

    void 'should find a "QuoteEntity" by id'() {
        given:
        def id = 'c0D_6QSvTdC8t95ALENRBg'
        def req = get("${Url.QUOTE}/${id}")
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
        res.andExpect(jsonPath('$.quote_id').value(id))
        res.andExpect(jsonPath('$.tags').value(['Music']))
        res.andExpect(jsonPath('$.updated_at').hasJsonPath())
        res.andExpect(jsonPath('$.value').value('I have as much chance of becoming Prime Minister as of being decapitated by a frisbee or of finding Elvis.'))

        and: 'body has an author embedded'
        res.andExpect(jsonPath('$._embedded.authors[0]._links.self.href').value('http://localhost/author/wVE8Y7BoRKCBkxs1JkqBvw'))
        res.andExpect(jsonPath('$._embedded.authors[0].author_id').value('wVE8Y7BoRKCBkxs1JkqBvw'))
        res.andExpect(jsonPath('$._embedded.authors[0].bio').value(null))
        res.andExpect(jsonPath('$._embedded.authors[0].created_at').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.authors[0].name').value('Boris Johnson'))
        res.andExpect(jsonPath('$._embedded.authors[0].slug').value('boris-johnson'))
        res.andExpect(jsonPath('$._embedded.authors[0].updated_at').hasJsonPath())

        and: 'body has a source embedded'
        res.andExpect(jsonPath('$._embedded.sources[0]._links.self.href').value('http://localhost/quote-source/in8rU9GMRxeeweuLiqz9yg'))
        res.andExpect(jsonPath('$._embedded.sources[0].created_at').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.sources[0].filename').value(null))
        res.andExpect(jsonPath('$._embedded.sources[0].quote_source_id').value('in8rU9GMRxeeweuLiqz9yg'))
        res.andExpect(jsonPath('$._embedded.sources[0].remarks').value('The Big Book of Boris'))
        res.andExpect(jsonPath('$._embedded.sources[0].updated_at').hasJsonPath())
        res.andExpect(jsonPath('$._embedded.sources[0].url').value('https://www.dailymail.co.uk'))

        where:
        acceptHeader << [
                'application/hal+json',
                'application/json'
        ]
    }

    void 'should report an error if "QuoteEntity" does not exist'() {
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
