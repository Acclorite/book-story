package ua.acclorite.book_story.data.parser

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.yield
import org.jsoup.nodes.Document
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.presentation.core.util.clearAllMarkdown
import ua.acclorite.book_story.presentation.core.util.clearMarkdown
import ua.acclorite.book_story.presentation.core.util.containsVisibleText
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject

class DocumentParser @Inject constructor(
    private val markdownParser: MarkdownParser
) {
    /**
     * Parses document to get it's text.
     * Fixes issues such as manual line breaking in <p>.
     * Applies Markdown to the text: Bold(**), Italic(_), Section separator(---), and Links(a > href).
     *
     * @return Parsed text line by line with Markdown(all lines are not blank).
     */
    suspend fun Document.parseDocument(
        zipFile: ZipFile? = null,
        imageEntries: List<ZipEntry>? = null,
        includeChapter: Boolean = true
    ): List<ReaderText> {
        yield()

        val readerText = mutableListOf<ReaderText>()
        var chapterAdded = false

        body().apply {
            // Remove manual line breaks from all <p>, <a>
            select("p, a").forEach { element ->
                yield()
                element.html(element.html().replace(Regex("\\n+"), " "))
            }

            // Remove <head>'s title
            select("title").remove()

            // Markdown
            select("hr").append("\n---\n")
            select("b").append("**").prepend("**")
            select("h1").append("**").prepend("**")
            select("h2").append("**").prepend("**")
            select("h3").append("**").prepend("**")
            select("strong").append("**").prepend("**")
            select("em").append("_").prepend("_")
            select("a").forEach { element ->
                var link = element.attr("href")
                if (!link.startsWith("http") || element.wholeText().isBlank()) return@forEach

                if (link.startsWith("http://")) {
                    link = link.replace("http://", "https://")
                }

                element.prepend("[")
                element.append("]($link)")
            }

            // Image
            select("img").forEach { element ->
                val src = element.attr("src")
                    .trim()
                    .substringAfterLast("/")
                    .lowercase()
                    .takeIf {
                        it.containsVisibleText() && imageEntries?.any { image ->
                            it == image.name.substringAfterLast('/').lowercase()
                        } == true
                    } ?: return@forEach

                val alt = element.attr("alt").trim().takeIf {
                    it.clearMarkdown().containsVisibleText()
                } ?: src.substringBeforeLast(".")

                element.append("\n[[$src|$alt]]\n")
            }
        }.wholeText().lines().forEach { line ->
            yield()

            val formattedLine = line.replace(
                Regex("""\*\*\*\s*(.*?)\s*\*\*\*"""), "_**$1**_"
            ).replace(
                Regex("""\*\*\s*(.*?)\s*\*\*"""), "**$1**"
            ).replace(
                Regex("""_\s*(.*?)\s*_"""), "_$1_"
            ).trim()

            val imageRegex = Regex("""\[\[(.*?)\|(.*?)]]""")

            if (line.containsVisibleText()) {
                when {
                    imageRegex.matches(line) -> {
                        val trimmedLine = line.removeSurrounding("[[", "]]")
                        val src = trimmedLine.substringBefore("|")
                        val alt = "_${trimmedLine.substringAfter("|")}_"

                        val image = try {
                            val imageEntry = imageEntries?.find { image ->
                                src == image.name.substringAfterLast('/').lowercase()
                            } ?: return@forEach

                            zipFile?.getImage(imageEntry)?.asImageBitmap()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        } ?: return@forEach

                        image.prepareToDraw()
                        readerText.add( // Adding image
                            ReaderText.Image(
                                imageBitmap = image
                            )
                        )
                        readerText.add( // Adding alternative text (caption) for image
                            ReaderText.Text(
                                markdownParser.parse(alt)
                            )
                        )
                    }

                    line == "---" || line == "***" -> readerText.add(ReaderText.Separator)

                    else -> {
                        if (
                            !chapterAdded &&
                            formattedLine.clearAllMarkdown().containsVisibleText() &&
                            includeChapter
                        ) {
                            readerText.add(
                                0, ReaderText.Chapter(
                                    title = formattedLine.clearAllMarkdown()
                                )
                            )
                            chapterAdded = true
                        } else if (
                            formattedLine.clearMarkdown().containsVisibleText()
                        ) {
                            readerText.add(
                                ReaderText.Text(
                                    line = markdownParser.parse(formattedLine)
                                )
                            )
                        }
                    }
                }
            }
        }

        yield()

        if (
            readerText.filterIsInstance<ReaderText.Text>().isEmpty() ||
            (includeChapter && readerText.filterIsInstance<ReaderText.Chapter>().isEmpty())
        ) {
            return emptyList()
        }

        return readerText
    }

    /**
     * Getting bitmap from [ZipFile] with compression
     * that depends on the [imageEntry] size.
     */
    private fun ZipFile.getImage(imageEntry: ZipEntry): Bitmap? {
        fun getBitmapFromInputStream(compressionLevel: Int = 1): Bitmap? {
            return getInputStream(imageEntry).use { inputStream ->
                BitmapFactory.decodeStream(
                    inputStream,
                    null,
                    BitmapFactory.Options().apply {
                        inPreferredConfig = Bitmap.Config.RGB_565
                        inSampleSize = compressionLevel
                    }
                )
            }
        }


        val uncompressedBitmap = getBitmapFromInputStream() ?: return null
        return when (uncompressedBitmap.byteCount) {
            in 0..1048576 /* 0 - 1MB */ -> {
                uncompressedBitmap
            }

            in 1048576..2097152 /* 1MB - 2MB */ -> {
                uncompressedBitmap.recycle()
                getBitmapFromInputStream(2)
            }

            else -> /* >=2MB */ {
                uncompressedBitmap.recycle()
                getBitmapFromInputStream(3)
            }
        }
    }
}