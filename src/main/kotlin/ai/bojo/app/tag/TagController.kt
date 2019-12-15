package ai.bojo.app.tag

import ai.bojo.app.Url
import ai.bojo.app.exception.EntityNotFoundException
import ai.bojo.app.search.PageModel
import org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RequestMapping(value = [Url.TAG])
@RestController
class TagController(
        private val assembler: TagModelAssembler,
        private val repository: TagRepository
) {

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
        val result: List<TagEntity> = repository.findAll()

        return assembler.toPageModel(result)
    }

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