package com.acclorite.books_history.data.parser

import android.graphics.BitmapFactory
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.Category
import com.acclorite.books_history.util.UIText
import nl.siegmann.epublib.epub.EpubReader
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
            val book = epubReader.readEpub(FileInputStream(file))

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
                lastOpened = null,
                category = Category.entries[0],
                coverImage = coverImage
            )
        } catch (e: Exception) {
            return null
        }
    }
}