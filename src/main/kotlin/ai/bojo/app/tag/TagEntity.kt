package ai.bojo.app.tag

import ai.bojo.app.quote.QuoteEntity
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import javax.persistence.*

@Entity
@Table(name = "tag")
open class TagEntity(
        @get:Basic
        @get:Column(name = "created_at")
        @get:CreationTimestamp
        open var createdAt: java.sql.Timestamp? = null,

        @get:JoinTable(
                name = "quote_tag",
                inverseJoinColumns = [JoinColumn(name = "quote_id")],
                joinColumns = [JoinColumn(name = "tag_id")]
        )
        @get:OneToMany(fetch = FetchType.LAZY)
        open var quotes: List<QuoteEntity>? = emptyList(),

        @get:Id
        @get:GenericGenerator(name = "slug_id", strategy = "ai.bojo.app.hibernate.SlugIdGenerator")
        @get:GeneratedValue(generator = "slug_id")
        @get:Column(name = "tag_id", nullable = false, insertable = false, updatable = false)
        open var tagId: String? = null,

        @get:Basic
        @get:Column(name = "updated_at")
        @get:UpdateTimestamp
        open var updatedAt: java.sql.Timestamp? = null,

        @get:Basic
        @get:Column(name = "value")
        open var value: String? = null
)

