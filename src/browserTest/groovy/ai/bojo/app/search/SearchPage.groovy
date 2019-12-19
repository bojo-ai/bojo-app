package ai.bojo.app.search

import ai.bojo.app.Url
import geb.Page

class SearchPage extends Page {
    static url = Url.SEARCH_QUOTE

    static content = {
        searchForm() { $("form[action='${Url.SEARCH_QUOTE}']") }
        searchResult() { $('#search-result-total') }
        searchInputField() { searchForm.find('input[name="query"]') }
        searchSubmitButton() { searchForm.find('input[type="submit"]') }
    }

    static at = {
        title.startsWith 'Search for:'
        searchForm.isDisplayed()
        searchResult.isDisplayed()
    }

    def total() {
        return searchResult
                .find('p > span')
                .text()
                .find(/\d+/)
                .toInteger()
    }
}
