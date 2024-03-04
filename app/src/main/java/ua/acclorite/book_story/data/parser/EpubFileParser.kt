package ua.acclorite.book_story.data.parser

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.epub.EpubReader
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.util.UIText
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class EpubFileParser @Inject constructor() : FileParser {

    override suspend fun parse(file: File): Book? {
        if (!file.name.endsWith(".epub")) {
            return null
        }

        try {
            val epubReader = EpubReader()
            val book = withContext(Dispatchers.IO) {
                epubReader.readEpub(FileInputStream(file))
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
                if (description.isEmpty()) {
                    description = null
                }
            }

            return Book(
                id = null,
                title = title,
                author = author,
                description = description?.toString(),
                text = emptyList(),
                progress = 0f,
                file = file,
                filePath = file.path,
                lastOpened = null,
                category = Category.entries[0],
                coverImage = coverImage
            )
        } catch (e: Exception) {
            return null
        }
    }
}