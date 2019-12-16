package ai.bojo.app.author

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import javax.persistence.*

@Entity
@Table(name = "author")
open class AuthorEntity(
        @get:Column(name = "author_id", nullable = false, insertable = false, updatable = false)
        @get:GeneratedValue(generator = "slug_id")
        @get:GenericGenerator(name = "slug_id", strategy = "ai.bojo.app.hibernate.SlugIdGenerator")
        @get:Id
        open var authorId: String? = null,

        @get:Basic
        @get:Column(name = "bio")
        open var bio: String? = null,

        @get:Basic
        @get:Column(name = "created_at")
        @get:CreationTimestamp
        open var createdAt: java.sql.Timestamp? = null,

        @get:Basic
        @get:Column(name = "name")
        open var name: String? = null,

        @get:Basic
        @get:Column(name = "slug")
        open var slug: String? = null,

        @get:Basic
        @get:Column(name = "updated_at")
        @get:UpdateTimestamp
        open var updatedAt: java.sql.Timestamp? = null
)