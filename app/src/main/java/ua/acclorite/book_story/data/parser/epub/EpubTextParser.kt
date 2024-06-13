package ua.acclorite.book_story.data.parser.epub

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.epub.EpubReader
import org.jsoup.Jsoup
import org.jsoup.nodes.Document.OutputSettings
import org.jsoup.safety.Safelist
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import javax.inject.Inject


class EpubTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<String>> {
        if (!file.name.endsWith(".epub", true) || !file.exists()) {
            return Resource.Error(UIText.StringResource(R.string.error_wrong_file_format))
        }

        try {
            val book = withContext(Dispatchers.IO) {
                EpubReader().readEpub(FileInputStream(file))
            }
            val unformattedText = StringBuilder()

            for (spineReference in book.spine.spineReferences) {
                val resource = spineReference.resource
                val inputStream = resource.inputStream
                val reader = BufferedReader(
                    InputStreamReader(
                        inputStream,
                        Charset.forName("UTF-8")
                    )
                )
                var line: String?

                withContext(Dispatchers.IO) {
                    inputStream.close()
                }

                while (withContext(Dispatchers.IO) { reader.readLine() }
                        .also { line = it } != null) {
                    unformattedText.append(line).append("\n")
                }

                withContext(Dispatchers.IO) {
                    reader.close()
                }
            }

            val strings = mutableListOf<String>()

            val parsedText = Jsoup.parse(unformattedText.toString())
            parsedText.outputSettings(OutputSettings().prettyPrint(false))
            parsedText.select("br").append("\n")
            parsedText.select("p").prepend("\n")
            parsedText.select("em").append(" ").prepend("")

            val formattedText = Jsoup.clean(
                parsedText.html(),
                "",
                Safelist.none(),
                OutputSettings().prettyPrint(false)
            )

            formattedText
                .replace("&nbsp;", "")
                .replace("\u00a0", "")
                .split("\n")
                .forEach {
                    if (it.isNotBlank()) {
                        strings.add(it.trim())
                    }
                }

            if (strings.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            return Resource.Success(strings)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(
                UIText.StringResource(
                    R.string.error_query,
                    e.message?.take(40)?.trim() ?: ""
                )
            )
        }
    }
}