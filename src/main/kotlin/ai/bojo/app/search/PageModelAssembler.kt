package ai.bojo.app.search

import ai.bojo.app.quote.QuoteEntity
import ai.bojo.app.quote.QuoteModelAssembler
import org.springframework.data.domain.Page
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.stereotype.Component

@Component
class PageModelAssembler(
        private val quoteAssembler: QuoteModelAssembler
) : RepresentationModelAssemblerSupport<Page<QuoteEntity>, PageModel>(SearchController::class.java, PageModel::class.java) {

    override fun toModel(page: Page<QuoteEntity>): PageModel {
        return instantiateModel(page)
    }

    override fun instantiateModel(page: Page<QuoteEntity>): PageModel {
        return PageModel(
                page.numberOfElements,
                page.totalElements,
                CollectionModel(
                        quoteAssembler.toCollectionModel(page.content)
                )
        )
    }
}