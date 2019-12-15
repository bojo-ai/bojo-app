package ai.bojo.app.quote

import ai.bojo.app.BaseSpecification
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
