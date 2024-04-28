package ua.acclorite.book_story.data.parser.epub

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.epub.EpubReader
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class EpubFileParser @Inject constructor() : FileParser {

    override suspend fun parse(file: File): Pair<Book, CoverImage?>? {
        if (!file.name.endsWith(".epub") || !file.exists()) {
            return null
        }

        try {
            val epubReader = EpubReader()
            val book = withContext(Dispatchers.IO) {
                FileInputStream(file).use {
                    epubReader.readEpub(it)
                }
            }

            val metadata = book.metadata
            val author = UIText.StringValue(metadata.authors.joinToString(", "))
            val title = metadata.titles.joinToString(", ")
            val coverImage = book.coverImage?.let { BitmapFactory.decodeStream(it.inputStream) }

            var description: StringBuilder? = StringBuilder()
            metadata.descriptions.forEach {
                description?.append(it)?.append(" ")
            }

            if (description != null) {
                if (description.isBlank()) {
                    description = null
                }
            }

            return Book(
                title = title,
                author = author,
                description = description?.toString(),
                textPath = "",
                scrollIndex = 0,
                scrollOffset = 0,
                progress = 0f,
                filePath = file.path,
                lastOpened = null,
                category = Category.entries[0],
                coverImage = null
            ) to coverImage
        } catch (e: Exception) {
            return null
        }
    }
}