package ai.bojo.app.meme

import ai.bojo.app.quote.QuoteEntity
import org.springframework.stereotype.Service
import java.io.File
import java.io.OutputStream
import java.util.concurrent.TimeUnit

@Service
class MemeGenerator {

    val border = 70
    val height = 800
    val width = 800
    val colorBackground = "#0F3244"
    val colorBlue = "#58C0F9"
    val colorWhite = "#FFFFFF"
    val font = File("src/main/resources/meme/font/bitstream-vera-sans/vera-bold.ttf")
    val logo = File("src/main/resources/meme/image/bojo_250x250.jpg")

    private fun execute(command: List<String>, outputStream: OutputStream) {
        val process = ProcessBuilder(command)
                .start()
                .also { it.waitFor(15L, TimeUnit.SECONDS) }

        if (process.exitValue() != 0) {
            val stderr = process
                    .errorStream
                    .bufferedReader()
                    .use { it.readText() }

            throw MemeException(stderr)
        }

        process.inputStream?.let { inputStream ->
            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()
        }
    }

    fun generate(
            outputStream: OutputStream,
            quote: QuoteEntity
    ) {
        val text = when {
            !quote.value.isNullOrEmpty() -> quote.value.toString()
            else -> ""
        }

        val command = listOf(
                "convert",

                // Create canvas
                "-font", font.path,
                "-gravity", "center",
                "-quality", "85",
                "-size", "${height - (2 * border)}x${width}!",

                // Add logo
                "-draw", "image over 0,-195 0,0 ${logo.path}",

                // Add quote
                "-background", colorWhite,
                "-fill", colorBackground,
                "-geometry", "+60+30",
                "-pointsize", "28",
                "-style", "normal",
                "caption:${text}",
                "-bordercolor", colorWhite,
                "-border", "${border}x0",

                // Add website
                "-fill", colorBlue,
                "-annotate", "+0+300", "bojo.ai",

                // Add top border
                "-fill", colorBlue,
                "-draw", "rectangle 0,5 1024,0",

                // Pipe to stdout
                "jpeg:-"
        )

        execute(command, outputStream)
    }
}
