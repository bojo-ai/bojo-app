package ai.bojo.app.quote_source

import ai.bojo.app.Url
import ai.bojo.app.exception.EntityNotFoundException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@Tag(name = "quote-source", description = "Service to retrieve and create quote sources")
@RequestMapping(value = [Url.QUOTE_SOURCE])
@RestController
class QuoteSourceController(
        private val assembler: QuoteSourceModelAssembler,
        private val repository: QuoteSourceRepository
) {

    @Operation(summary = "Find a quote sources by its id", tags = ["quote-source"])
    @ResponseBody
    @RequestMapping(
            headers = [
                "${HttpHeaders.ACCEPT}=${MediaType.APPLICATION_JSON_VALUE}",
                "${HttpHeaders.ACCEPT}=$HAL_JSON_VALUE"
            ],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            value = ["/{id}"]
    )
    fun findById(
            @RequestHeader(HttpHeaders.ACCEPT) acceptHeader: String,
            @PathVariable id: String
    ): QuoteSourceModel {
        val entity = repository.findById(id).orElseThrow {
            EntityNotFoundException("QuoteSource with id \"$id\" not found.")
        }

        return assembler.toModel(entity)
    }
}