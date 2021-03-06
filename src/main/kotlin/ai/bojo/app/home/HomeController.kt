package ai.bojo.app.home

import ai.bojo.app.Url
import ai.bojo.app.quote.QuoteRepository
import ai.bojo.app.tag.TagModelAssembler
import ai.bojo.app.tag.TagRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class HomeController(
        val quoteRepository: QuoteRepository,
        val tagModelAssembler: TagModelAssembler,
        val tagRepository: TagRepository
) {

    @RequestMapping(
            headers = [HttpHeaders.ACCEPT + "=" + MediaType.TEXT_HTML_VALUE],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_HTML_VALUE],
            value = [Url.INDEX]
    )
    fun get(): ModelAndView {
        val quote = quoteRepository.randomQuote().get()
        val tags = tagRepository.findByOrderByValueAsc()
        val tagModels = tagModelAssembler.toCollectionModel(tags)

        return ModelAndView("home")
                .addObject("quote", quote)
                .addObject("tags", tagModels.content)
    }
}