package ua.acclorite.book_story.data.parser.txt

import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.util.UIText
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
                filePath = file.path,
                lastOpened = null,
                category = Category.entries[0],
                coverImage = null
            )
        } catch (e: Exception) {
            return null
        }
    }
}