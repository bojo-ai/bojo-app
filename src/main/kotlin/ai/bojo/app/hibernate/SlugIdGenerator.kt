package ai.bojo.app.hibernate

import ai.bojo.app.util.SlugId
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator

class SlugIdGenerator : IdentifierGenerator {

    override fun generate(session: SharedSessionContractImplementor?, entity: Any?): String {
        return SlugId.create()
    }
}