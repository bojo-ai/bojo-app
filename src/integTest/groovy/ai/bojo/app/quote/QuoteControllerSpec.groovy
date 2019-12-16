package ai.bojo.app.quote

import ai.bojo.app.BaseSpecification
import ai.bojo.app.author.AuthorEntity
import ai.bojo.app.quote_source.QuoteSourceEntity
import ai.bojo.app.tag.TagEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import spock.lang.Subject

import java.sql.Timestamp

@SpringBootTest
class QuoteControllerSpec extends BaseSpecification {

    @Autowired
    @Subject
    QuoteController controller

    def 'should create a new QuoteEntity'() {
        given:9
        def author = new AuthorEntity(name: 'Boris', slug: 'boris')
        def appearedAt = new Timestamp(new Date().time)
        def source = new QuoteSourceEntity(url: 'https://www.telegraph.co.uk/news/2016/05/14/boris-johnson-the-eu-wants-a-superstate-just-as-hitler-did/')
        def tag = new TagEntity(value: 'Napoleon')
        def value = 'Napoleon, Hitler, various people tried this out, and it ends tragically. The EU is an attempt to do this by different methods.'
        def entity = new QuoteEntity(author: author, appearedAt: appearedAt, source: source, tags: [tag], value: value)

        when:
        def response = controller.create(entity)

        then:
        response.appearedAt == appearedAt
        response.createdAt != null
        response.embedded.content.getAt(0).name == 'Boris'
        response.embedded.content.getAt(1).url == 'https://www.telegraph.co.uk/news/2016/05/14/boris-johnson-the-eu-wants-a-superstate-just-as-hitler-did/'
        response.tags == ['Napoleon']
        response.updatedAt != null
        response.value == value
    }

    def 'should find "QuoteEntity" by id'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE
        def id = 'c0D_6QSvTdC8t95ALENRBg'

        when:
        def response = controller.findById(acceptHeader, id)

        then:
        response.appearedAt == Timestamp.valueOf('2003-07-22 00:00:00.000000')
        response.createdAt != null
        response.updatedAt != null
        response.quoteId == id
        response.tags.get(0) == 'Music'
        response.value == 'I have as much chance of becoming Prime Minister as of being decapitated by a frisbee or of finding Elvis.'
    }

    def 'should report an error if "QuoteEntity" does not exist'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE
        def id = 'does-not-exist'

        when:
        controller.findById(acceptHeader, id)

        then:
        def exception = thrown(ai.bojo.app.exception.EntityNotFoundException)
        exception.message == 'Quote with id "does-not-exist" not found.'
    }
}
