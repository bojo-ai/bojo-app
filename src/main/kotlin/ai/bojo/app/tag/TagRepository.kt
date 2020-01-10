package ai.bojo.app.tag

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TagRepository : JpaRepository<TagEntity, String> {

    fun findByOrderByValueAsc(): List<TagEntity>

    fun findByValue(value: String): Optional<TagEntity>
}
