package ai.bojo.app.search

import ai.bojo.app.BaseSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import spock.lang.Subject

class SearchControllerSpec extends BaseSpecification {

    @Autowired
    @Subject
    SearchController controller

    def 'should find "PageModel" by query'() {
        given:
        def acceptHeader = MediaType.APPLICATION_JSON_VALUE
        def pageNumber = 0
        def query = 'Elvis'

        when:
        def response = controller.quote(acceptHeader, query, pageNumber)

        then:
        response.count == 1
        response.embedded != null
        response.links != null
        response.total == 1

        response.getLink("self").get().getHref() == 'http://localhost/search/quote?query=Elvis&page=0'
        response.getLink("first").get().getHref() == 'http://localhost/search/quote?query=Elvis&page=0'
        response.getLink("prev").get().getHref() == 'http://localhost/search/quote?query=Elvis&page=0'
        response.getLink("next").get().getHref() == 'http://localhost/search/quote?query=Elvis&page=1'
        response.getLink("last").get().getHref() == 'http://localhost/search/quote?query=Elvis&page=1'
    }
}
