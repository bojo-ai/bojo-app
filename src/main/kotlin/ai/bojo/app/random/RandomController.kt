package ai.bojo.app.random

import ai.bojo.app.Url
import ai.bojo.app.meme.MemeGenerator
import ai.bojo.app.quote.QuoteModel
import ai.bojo.app.quote.QuoteModelAssembler
import ai.bojo.app.quote.QuoteRepository
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

@RequestMapping(value = [Url.RANDOM])
@RestController
class RandomController(
        private val assembler: QuoteModelAssembler,
        private val memeGenerator: MemeGenerator,
        private val repository: QuoteRepository
) {

    @ResponseBody
    @RequestMapping(
            headers = [
                "${HttpHeaders.ACCEPT}=${MediaType.IMAGE_JPEG_VALUE}"
            ],
            method = [RequestMethod.GET],
            value = ["/meme"]
    )
    fun meme(): ResponseEntity<StreamingResponseBody> {
        var quote = repository.randomQuote().get()

        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        headers.set("X-Quote-Id", quote.quoteId)

        val stream = StreamingResponseBody {
            memeGenerator.generate(it, quote)
        }

        return ResponseEntity(
                stream,
                headers,
                HttpStatus.OK
        )
    }

    @ResponseBody
    @RequestMapping(
            headers = [
                "${HttpHeaders.ACCEPT}=${MediaType.APPLICATION_JSON_VALUE}",
                "${HttpHeaders.ACCEPT}=${MediaTypes.HAL_JSON_VALUE}"
            ],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            value = ["/quote"]
    )
    fun random(@RequestHeader(HttpHeaders.ACCEPT) acceptHeader: String): QuoteModel {
        return assembler.toModel(
                repository.randomQuote().get()
        )
    }
}
