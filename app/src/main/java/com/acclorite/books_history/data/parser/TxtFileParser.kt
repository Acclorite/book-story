package com.acclorite.books_history.data.parser

import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.Category
import com.acclorite.books_history.domain.model.StringWithId
import com.acclorite.books_history.util.UIText
import java.io.File
import javax.inject.Inject

class TxtFileParser @Inject constructor() : FileParser {

    override suspend fun parse(file: File): Book? {
        if (!file.name.endsWith(".txt")) {
            return null
        }

        try {
            val title = file.name.trim().dropLast(4)
            val author = UIText.StringResource(R.string.unknown_author)
            val text = emptyList<StringWithId>()

            return Book(
                id = null,
                title = title,
                author = author,
                description = null,
                text = text,
                progress = 0f,
                file = file,
                lastOpened = null,
                category = Category.entries[0],
                coverImage = null
            )
        } catch (e: Exception) {
            return null
        }
    }
}