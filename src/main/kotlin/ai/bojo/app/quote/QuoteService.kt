package ai.bojo.app.quote

import ai.bojo.app.author.AuthorRepository
import ai.bojo.app.quote_source.QuoteSourceRepository
import ai.bojo.app.tag.TagRepository
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class QuoteService(
        private val authorRepository: AuthorRepository,
        private val quoteRepository: QuoteRepository,
        private val quoteSourceRepository: QuoteSourceRepository,
        private val tagRepository: TagRepository
) {

    fun save(quoteEntity: QuoteEntity): QuoteEntity {
        if (quoteEntity.author?.authorId == null) {
            quoteEntity.author = authorRepository.saveAndFlush(quoteEntity.author!!)
        }
        if (quoteEntity.source?.quoteSourceId == null && quoteEntity.source?.url != null) {
            quoteSourceRepository.saveAndFlush(quoteEntity.source!!)
        }

        quoteEntity.tags?.stream()
                ?.filter { tag -> tag.tagId == null && tag.value != null }
                ?.map { tag -> tagRepository.saveAndFlush(tag) }
                ?.toList()

        return quoteRepository.saveAndFlush(quoteEntity)
    }
}
