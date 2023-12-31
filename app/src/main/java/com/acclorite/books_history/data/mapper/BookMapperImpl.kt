package com.acclorite.books_history.data.mapper

import com.acclorite.books_history.data.local.dto.BookEntity
import com.acclorite.books_history.domain.model.Book
import javax.inject.Inject

class BookMapperImpl @Inject constructor(): BookMapper {
    override fun toBookEntity(book: Book): BookEntity {
        TODO("Not yet implemented")
    }

    override fun toBook(bookEntity: BookEntity): Book {
        TODO("Not yet implemented")
    }
}