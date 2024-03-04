package ua.acclorite.book_story.data.mapper

import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.domain.model.Book

interface BookMapper {
    suspend fun toBookEntity(book: Book): BookEntity

    suspend fun toBook(bookEntity: BookEntity): Book
}