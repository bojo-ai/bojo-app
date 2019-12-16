package ai.bojo.app.quote_source

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import javax.persistence.*

@Entity
@Table(name = "quote_source")
open class QuoteSourceEntity(
        @get:Basic
        @get:Column(name = "created_at")
        @get:CreationTimestamp
        open var createdAt: java.sql.Timestamp? = null,

        @get:Basic
        @get:Column(name = "filename")
        open var filename: String? = null,

        @get:Column(name = "quote_source_id", nullable = false, insertable = false, updatable = false)
        @get:GeneratedValue(generator = "slug_id")
        @get:GenericGenerator(name = "slug_id", strategy = "ai.bojo.app.hibernate.SlugIdGenerator")
        @get:Id
        open var quoteSourceId: String? = null,

        @get:Basic
        @get:Column(name = "remarks")
        open var remarks: String? = null,

        @get:Basic
        @get:Column(name = "updated_at")
        @get:UpdateTimestamp
        open var updatedAt: java.sql.Timestamp? = null,

        @get:Basic
        @get:Column(name = "url")
        open var url: String? = null
)
