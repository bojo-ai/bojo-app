package ai.bojo.app.tag

import ai.bojo.app.Url
import ai.bojo.app.exception.EntityNotFoundException
import ai.bojo.app.search.PageModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RequestMapping(value = [Url.TAG])
@RestController
@Tag(name = "tag", description = "Service to retrieve and create tags")
class TagController(
        private val assembler: TagModelAssembler,
        private val repository: TagRepository
) {

    @Operation(summary = "Create a new tag", tags = ["tag"])
    @ResponseBody
    @RequestMapping(
            headers = [
                "${HttpHeaders.ACCEPT}=${MediaType.APPLICATION_JSON_VALUE}"
            ],
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(@RequestBody tag: TagEntity): ResponseEntity<TagModel> {
        val entity = repository.saveAndFlush(tag)

        return ResponseEntity(
                assembler.toModel(entity),
                HttpStatus.CREATED
        )
    }

    @Operation(summary = "Find all tags", tags = ["tag"])
    @ResponseBody
    @RequestMapping(
            headers = [
                "${HttpHeaders.ACCEPT}=${MediaType.APPLICATION_JSON_VALUE}",
                "${HttpHeaders.ACCEPT}=$HAL_JSON_VALUE"
            ],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findAll(
            @RequestHeader(HttpHeaders.ACCEPT) acceptHeader: String
    ): PageModel {
        val result: List<TagEntity> = repository.findByOrderByValueAsc()

        return assembler.toPageModel(result)
    }

    @Operation(summary = "Find a tag by its id", tags = ["tag"])
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
    ): TagModel {
        val entity = repository.findById(id).orElseThrow {
            EntityNotFoundException("Tag with id \"$id\" not found.")
        }

        return assembler.toModel(entity)
    }

    @Operation(summary = "Find a tag by its value", tags = ["tag"])
    @ResponseBody
    @RequestMapping(
            headers = [
                "${HttpHeaders.ACCEPT}=${MediaType.TEXT_HTML_VALUE}"
            ],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_HTML_VALUE],
            value = ["/{value}"]
    )
    fun findByValue(@PathVariable value: String): ModelAndView {
        val entity = repository.findByValue(value).orElseThrow {
            EntityNotFoundException("Tag with value \"$value\" not found.")
        }

        return ModelAndView("tag")
                .addObject("tag", entity)
    }
}