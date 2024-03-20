package ua.acclorite.book_story.data.parser.epub

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.epub.EpubReader
import org.jsoup.Jsoup
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import javax.inject.Inject

class EpubTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<StringWithId>> {
        if (!file.name.endsWith(".epub")) {
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

                while (withContext(Dispatchers.IO) {
                        reader.readLine()
                    }.also { line = it } != null) {
                    unformattedText.append(line).append("\n")
                }

                withContext(Dispatchers.IO) {
                    reader.close()
                }
            }

            val stringWithIds = mutableListOf<StringWithId>()

            Jsoup.parse(unformattedText.toString()).wholeText().split("\n").forEach {
                if (it.isNotBlank()) {
                    stringWithIds.add(StringWithId(it.trim()))
                }
            }

            if (stringWithIds.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            return Resource.Success(stringWithIds)
        } catch (e: Exception) {
            return Resource.Error(
                UIText.StringResource(
                    R.string.error_query,
                    e.message?.take(40)?.trim() ?: ""
                )
            )
        }
    }
}