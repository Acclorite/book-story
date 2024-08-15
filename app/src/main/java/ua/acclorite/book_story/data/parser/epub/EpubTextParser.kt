package ua.acclorite.book_story.data.parser.epub

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import java.util.zip.ZipFile
import javax.inject.Inject


class EpubTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<String>> {
        if (!file.name.endsWith(".epub", true) || !file.exists()) {
            return Resource.Error(UIText.StringResource(R.string.error_wrong_file_format))
        }

        try {
            val lines = mutableListOf<String>()

            withContext(Dispatchers.IO) {
                ZipFile(file).use { zip ->
                    zip.entries().asSequence().forEach { entry ->
                        if (
                            entry.name.endsWith(".xhtml")
                            || entry.name.endsWith(".html")
                            || entry.name.endsWith(".xml")
                            || entry.name.endsWith(".htm")
                        ) {
                            val content = zip.getInputStream(entry).bufferedReader()
                                .use {
                                    it.readText()
                                }

                            val document = Jsoup.parse(content)
                            document.select("p").append("\n")
                            document
                                .wholeText()
                                .lines()
                                .forEach { line ->
                                    if (line.isNotBlank()) {
                                        lines.add(line.trim())
                                    }
                                }
                        }
                    }
                }
            }

            if (lines.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            return Resource.Success(lines)
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