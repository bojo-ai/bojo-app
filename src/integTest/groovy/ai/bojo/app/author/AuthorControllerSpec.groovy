package ai.bojo.app.author

import ai.bojo.app.BaseSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import spock.lang.Subject

@SpringBootTest
class AuthorControllerSpec extends BaseSpecification {

    @Autowired
    @Subject
    AuthorController controller

    def 'should find "AuthorEntity" by id'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE
        def id = 'wVE8Y7BoRKCBkxs1JkqBvw'

        when:
        def response = controller.findById(acceptHeader, id)

        then:
        response.authorId == id
        response.bio == null
        response.createdAt != null
        response.name == 'Boris Johnson'
        response.slug == 'boris-johnson'
        response.updatedAt != null
    }

    def 'should report an error if "AuthorEntity" does not exist'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE
        def id = 'does-not-exist'

        when:
        controller.findById(acceptHeader, id)

        then:
        def exception = thrown(ai.bojo.app.exception.EntityNotFoundException)
        exception.message == 'Author with id "does-not-exist" not found.'
    }
}
