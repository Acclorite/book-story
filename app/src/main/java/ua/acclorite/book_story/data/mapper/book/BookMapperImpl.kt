package ua.acclorite.book_story.data.mapper.book

import android.net.Uri
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.util.UIText
import java.io.File
import javax.inject.Inject

class BookMapperImpl @Inject constructor() : BookMapper {
    override suspend fun toBookEntity(book: Book): BookEntity {
        val textAsString = book.text.joinToString("\n") {
            it.line.trim()
        }

        return BookEntity(
            id = book.id,
            title = book.title,
            filePath = book.filePath,
            scrollIndex = book.scrollIndex,
            scrollOffset = book.scrollOffset,
            progress = book.progress,
            author = book.author.string,
            text = textAsString,
            description = book.description,
            image = if (book.coverImage != null) book.coverImage.toString() else null,
            category = book.category
        )
    }

    override suspend fun toBook(bookEntity: BookEntity): Book {
        val file = File(bookEntity.filePath)

        return Book(
            id = bookEntity.id,
            title = bookEntity.title,
            author = bookEntity.author?.let { UIText.StringValue(it) } ?: UIText.StringResource(
                R.string.unknown_author
            ),
            description = bookEntity.description,
            scrollIndex = bookEntity.scrollIndex,
            scrollOffset = bookEntity.scrollOffset,
            progress = bookEntity.progress,
            file = if (file.exists()) file else null,
            text = emptyList(),
            letters = 0,
            words = 0,
            filePath = bookEntity.filePath,
            lastOpened = null,
            category = bookEntity.category,
            coverImage = if (bookEntity.image != null) Uri.parse(bookEntity.image) else null
        )
    }
}