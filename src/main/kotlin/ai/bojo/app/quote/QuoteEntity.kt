package ai.bojo.app.quote

import ai.bojo.app.author.AuthorEntity
import ai.bojo.app.quote_source.QuoteSourceEntity
import ai.bojo.app.tag.TagEntity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import javax.persistence.*

@Entity
@JsonIgnoreProperties(ignoreUnknown = true) // Jackson annotation to ignore unknown attributes when deserializing JSON.
@Table(name = "quote")
open class QuoteEntity(
        @get:Basic
        @get:Column(name = "appeared_at")
        open var appearedAt: Timestamp? = null,

        @get:JoinColumn(name = "author_id", referencedColumnName = "author_id")
        @get:ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        open var author: AuthorEntity? = null,

        @get:Basic
        @get:Column(name = "created_at")
        @get:CreationTimestamp
        open var createdAt: Timestamp? = null,

        @get:Column(name = "quote_id", nullable = false, insertable = false, updatable = false)
        @get:GeneratedValue(generator = "slug_id")
        @get:GenericGenerator(name = "slug_id", strategy = "ai.bojo.app.hibernate.SlugIdGenerator")
        @get:Id
        open var quoteId: String? = null,

        @get:JoinColumn(name = "quote_source_id", referencedColumnName = "quote_source_id")
        @get:ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        open var source: QuoteSourceEntity? = null,

        @get:JoinTable(
                name = "quote_tag",
                joinColumns = [JoinColumn(name = "quote_id")],
                inverseJoinColumns = [JoinColumn(name = "tag_id")]
        )
        @get:OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        open var tags: List<TagEntity>? = emptyList(),

        @get:Basic
        @get:Column(name = "updated_at")
        @get:UpdateTimestamp
        open var updatedAt: Timestamp? = null,

        @get:Basic
        @get:Column(name = "value")
        open var value: String? = null
)

