package ai.bojo.app.quote

import ai.bojo.app.Url
import ai.bojo.app.author.AuthorRepository
import ai.bojo.app.exception.EntityNotFoundException
import ai.bojo.app.tag.TagRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RequestMapping(value = [Url.QUOTE])
@RestController
@Tag(name = "quote", description = "Service to retrieve and create quotes")
class QuoteController(
        private val assembler: QuoteModelAssembler,
        private val authorRepository: AuthorRepository,
        private val repository: QuoteRepository,
        private val service: QuoteService,
        private val tagRepository: TagRepository
) {

    @ApiResponses(value = [
        ApiResponse(responseCode = "201", content = [
            Content(schema = Schema(implementation = QuoteModel::class))
        ])
    ])
    @Operation(summary = "Create a new quote", tags = ["quote"])
    @ResponseBody
    @RequestMapping(
            headers = ["${HttpHeaders.ACCEPT}=${MediaType.APPLICATION_JSON_VALUE}"],
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(
            @RequestHeader(HttpHeaders.ACCEPT) acceptHeader: String,
            @RequestBody quote: QuoteEntity
    ): ResponseEntity<QuoteModel> {
        val entity = service.save(quote)

        return ResponseEntity(
                assembler.toModel(entity),
                HttpStatus.CREATED
        )
    }

    @Operation(hidden = true)
    @RequestMapping(
            headers = ["${HttpHeaders.ACCEPT}=${MediaType.APPLICATION_FORM_URLENCODED_VALUE}"],
            method = [RequestMethod.POST],
            produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun create(@ModelAttribute quote: QuoteEntity): ModelAndView {
        val entity = service.save(quote)

        return ModelAndView("quote")
                .addObject("quote", entity)
    }

    @Operation(hidden = true)
    @RequestMapping(
            headers = [
                "${HttpHeaders.ACCEPT}=${MediaType.TEXT_HTML_VALUE}"
            ],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_HTML_VALUE],
            value = ["/new"]
    )
    fun create(): ModelAndView {
        val authors = authorRepository.findAll()
        val tags = tagRepository.findAll()

        return ModelAndView("quote/new")
                .addObject("authors", authors)
                .addObject("quote", QuoteEntity())
                .addObject("tags", tags)
    }

    @Operation(summary = "Find a quote by its id", tags = ["quote"])
    @ResponseBody
    @RequestMapping(
            headers = [
                "${HttpHeaders.ACCEPT}=$HAL_JSON_VALUE",
                "${HttpHeaders.ACCEPT}=${MediaType.APPLICATION_JSON_VALUE}",
                "${HttpHeaders.ACCEPT}=${MediaType.TEXT_HTML_VALUE}"
            ],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE],
            value = ["/{id}"]
    )
    fun findById(
            @RequestHeader(HttpHeaders.ACCEPT) acceptHeader: String,
            @PathVariable id: String
    ): Any {
        val entity = repository.findById(id).orElseThrow {
            EntityNotFoundException("Quote with id \"$id\" not found.")
        }

        if (acceptHeader.contains(MediaType.TEXT_HTML_VALUE)) {
            return ModelAndView("quote")
                    .addObject("quote", entity)
        }

        return assembler.toModel(entity)
    }
}
