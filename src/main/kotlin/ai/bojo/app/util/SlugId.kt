package ai.bojo.app.util

import java.nio.ByteBuffer
import java.util.*

class SlugId {

    companion object Factory {
        private val decoder: Base64.Decoder = Base64.getUrlDecoder()
        private val encoder: Base64.Encoder = Base64.getUrlEncoder().withoutPadding()

        @JvmStatic
        fun create(): String {
            return create(
                    UUID.randomUUID()
            )
        }

        @JvmStatic
        fun create(uuid: UUID): String {
            val bytes = toBytes(uuid)

            return encoder.encodeToString(bytes)
        }

        private fun toBytes(uuid: UUID): ByteArray {
            val buffer: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
            buffer.putLong(uuid.leastSignificantBits)
            buffer.putLong(uuid.mostSignificantBits)

            return buffer.array()
        }

        @JvmStatic
        fun toUUID(slugId: String?): UUID {
            return uuidFromBytes(
                    decoder.decode(slugId)
            )
        }

        private fun uuidFromBytes(decoded: ByteArray?): UUID {
            val buffer: ByteBuffer = ByteBuffer.wrap(decoded)
            val leastSignificantBits: Long = buffer.long
            val mostSignificantBits: Long = buffer.long

            return UUID(mostSignificantBits, leastSignificantBits)
        }
    }
}