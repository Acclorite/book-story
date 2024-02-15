package com.acclorite.books_history.data.mapper

import com.acclorite.books_history.data.local.dto.BookEntity
import com.acclorite.books_history.data.local.room.BookDao
import com.acclorite.books_history.domain.model.Book

interface BookMapper {
    suspend fun toBookEntity(book: Book, database: BookDao): BookEntity

    suspend fun toBook(bookEntity: BookEntity): Book
}