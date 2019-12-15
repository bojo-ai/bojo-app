package ai.bojo.app.tag

import ai.bojo.app.quote.QuoteEntity
import javax.persistence.*

@Entity
@Table(name = "tag")
open class TagEntity(
        @get:Basic
        @get:Column(name = "created_at")
        open var createdAt: java.sql.Timestamp? = null,

        @get:JoinTable(
                name = "quote_tag",
                inverseJoinColumns = [JoinColumn(name = "quote_id")],
                joinColumns = [JoinColumn(name = "tag_id")]
        )
        @get:OneToMany(fetch = FetchType.LAZY)
        open var quotes: List<QuoteEntity>? = emptyList(),

        @get:Id
        @get:Column(name = "tag_id", nullable = false, insertable = false, updatable = false)
        open var tagId: String? = null,

        @get:Basic
        @get:Column(name = "updated_at")
        open var updatedAt: java.sql.Timestamp? = null,

        @get:Basic
        @get:Column(name = "value")
        open var value: String? = null
)

