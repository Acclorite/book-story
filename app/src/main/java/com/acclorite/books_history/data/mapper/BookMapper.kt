package com.acclorite.books_history.data.mapper

import com.acclorite.books_history.data.local.dto.BookEntity
import com.acclorite.books_history.domain.model.Book

interface BookMapper {
    fun Book.toBookEntity(): BookEntity

    fun BookEntity.toBook(): Book
}