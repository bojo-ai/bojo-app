package ai.bojo.app.home

import ai.bojo.app.Url
import geb.Page

class HomePage extends Page {
    static url = Url.INDEX

    static content = {
        searchForm() {
            $("form[action='${Url.SEARCH_QUOTE}']")
        }
        searchInputField() { searchForm.find('input[name="query"]') }
        searchSubmitButton() { searchForm.find('input[type="submit"]') }
    }

    static at = {
        title.startsWith 'BoJo'
        searchForm.isDisplayed()
    }

    def typeInSearchInputField(query) {
        searchInputField.value(query)
    }

    def clickOnSearchSubmitButton() {
        searchSubmitButton.click()
    }
}
