package ai.bojo.app.hibernate

import org.hibernate.engine.spi.SharedSessionContractImplementor
import spock.lang.Specification
import spock.lang.Subject

class SlugIdGeneratorSpec extends Specification {

    Object entity = Mock()
    SharedSessionContractImplementor session = Mock()

    @Subject
    SlugIdGenerator subject = new SlugIdGenerator()

    def 'should return a new slug identifier'() {
        when:
        def slugId = subject.generate(session, entity)

        then:
        slugId.matches('^[a-zA-Z0-9_-]{22}$')
    }
}
