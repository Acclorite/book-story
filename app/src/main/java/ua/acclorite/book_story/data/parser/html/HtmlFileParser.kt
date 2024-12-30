package ua.acclorite.book_story.data.parser.html

import org.jsoup.Jsoup
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.library.book.BookWithCover
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.domain.ui.UIText
import java.io.File
import javax.inject.Inject

class HtmlFileParser @Inject constructor() : FileParser {

    override suspend fun parse(file: File): BookWithCover? {
        return try {
            val document = Jsoup.parse(file)

            val title = document.select("head > title").text().trim().run {
                ifBlank {
                    file.nameWithoutExtension.trim()
                }
            }

            BookWithCover(
                book = Book(
                    title = title,
                    author = UIText.StringResource(R.string.unknown_author),
                    description = null,
                    scrollIndex = 0,
                    scrollOffset = 0,
                    progress = 0f,
                    filePath = file.path,
                    lastOpened = null,
                    category = Category.entries[0],
                    coverImage = null
                ),
                coverImage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}