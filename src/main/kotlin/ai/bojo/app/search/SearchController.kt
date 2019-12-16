package ai.bojo.app.search

import ai.bojo.app.Url
import ai.bojo.app.quote.QuoteEntity
import ai.bojo.app.quote.QuoteRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView


@RequestMapping(value = [Url.SEARCH])
@RestController
class SearchController(
        private val assembler: PageModelAssembler,
        private val repository: QuoteRepository
) {

    @ResponseBody
    @RequestMapping(
            headers = [
                "${HttpHeaders.ACCEPT}=${MediaType.APPLICATION_JSON_VALUE}",
                "${HttpHeaders.ACCEPT}=${MediaType.TEXT_HTML_VALUE}",
                "${HttpHeaders.ACCEPT}=${MediaTypes.HAL_JSON_VALUE}"
            ],
            method = [RequestMethod.GET],
            produces = [
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.TEXT_HTML_VALUE
            ],
            value = ["/quote"]
    )
    fun quote(
            @RequestHeader(HttpHeaders.ACCEPT) acceptHeader: String,
            @RequestParam("query") query: String,
            @RequestParam(value = "page", defaultValue = "0") pageNumber: Int
    ): Any {
        val page: Pageable = PageRequest.of(pageNumber, 10)
        val result: Page<QuoteEntity> = repository.findByValueContainingIgnoreCase(query, page)
        val model: PageModel = assembler.toModel(result)

        val linkBuilder: WebMvcLinkBuilder = linkTo(this::class.java)
        model.add(linkBuilder.slash("quote/?query=${query}&page=${pageNumber}").withSelfRel())
        model.add(linkBuilder.slash("quote/?query=${query}&page=${page.first().pageNumber}").withRel("first"))
        model.add(linkBuilder.slash("quote/?query=${query}&page=${page.previousOrFirst().pageNumber}").withRel("prev"))
        model.add(linkBuilder.slash("quote/?query=${query}&page=${page.next().pageNumber}").withRel("next"))
        model.add(linkBuilder.slash("quote/?query=${query}&page=${result.totalPages}").withRel("last"))

        if (acceptHeader.contains(MediaType.TEXT_HTML_VALUE)) {
            return ModelAndView("search")
                    .addObject("model", model)
                    .addObject("query", query)
                    .addObject("result", result)
        }

        return model
    }
}
