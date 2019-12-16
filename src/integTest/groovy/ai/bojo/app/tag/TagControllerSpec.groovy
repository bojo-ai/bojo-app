package ai.bojo.app.tag

import ai.bojo.app.BaseSpecification
import ai.bojo.app.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import spock.lang.Stepwise
import spock.lang.Subject

@SpringBootTest
@Stepwise
class TagControllerSpec extends BaseSpecification {

    @Autowired
    @Subject
    TagController controller

    def 'should create a new TagEntity'() {
        given:
        def tag = new TagEntity()
        tag.value = 'Jeremy Corbyn'

        when:
        def response = controller.create(tag)

        then:
        response.createdAt != null
        response.tagId != null
        response.value == 'Jeremy Corbyn'
        response.updatedAt != null
    }

    def 'should find "TagEntity" by id'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE
        def id = 'c1dIgMTURW-LllIZWSBESa'

        when:
        def response = controller.findById(acceptHeader, id)

        then:
        response.createdAt != null
        response.tagId == id
        response.value == 'Music'
        response.updatedAt != null
    }

    def 'should report an error if "QuoteEntity" does not exist'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE
        def id = 'does-not-exist'

        when:
        controller.findById(acceptHeader, id)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == 'Tag with id "does-not-exist" not found.'
    }

    def 'should find "PageModel" for all tags'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE

        when:
        def response = controller.findAll(acceptHeader)

        then:
        response.count == 2
        response.embedded != null
        response.total == 2
    }
}
