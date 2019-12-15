package ai.bojo.app.home

import ai.bojo.app.search.SearchPage
import geb.spock.GebSpec

class HomeSpec extends GebSpec {

    def "Search the dump"() {
        given: "you are on the homepage"
        HomePage homepage = to HomePage

        when: "you fill in the search input"
        homepage.typeInSearchInputField("Elvis")

        and: "you submit the form"
        homepage.clickOnSearchSubmitButton()

        then: "you will be redirected to the search result page"
        SearchPage searchPage = at SearchPage
        searchPage.title == 'Search for: Elvis'

        and: "you will see the total number of search results"
        searchPage.total() == 1
    }
}