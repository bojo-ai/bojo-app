package ai.bojo.app.quote_source

import ai.bojo.app.BaseSpecification
import ai.bojo.app.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import spock.lang.Subject

@SpringBootTest
class QuoteSourceControllerSpec extends BaseSpecification {

    @Autowired
    @Subject
    QuoteSourceController controller

    def 'should find "QuoteSourceEntity" by id'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE
        def id = 'in8rU9GMRxeeweuLiqz9yg'

        when:
        def response = controller.findById(acceptHeader, id)

        then:
        response.createdAt != null
        response.filename == null
        response.quoteSourceId == id
        response.remarks == 'The Big Book of Boris'
        response.updatedAt != null
        response.url == 'https://www.dailymail.co.uk'
    }

    def 'should report an error if "QuoteSourceEntity" does not exist'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE
        def id = 'does-not-exist'

        when:
        controller.findById(acceptHeader, id)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == 'QuoteSource with id "does-not-exist" not found.'
    }
}
