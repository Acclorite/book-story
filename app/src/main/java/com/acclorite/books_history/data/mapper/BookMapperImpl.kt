package com.acclorite.books_history.data.mapper

import com.acclorite.books_history.data.local.dto.BookEntity
import com.acclorite.books_history.domain.model.Book
import javax.inject.Inject

class BookMapperImpl @Inject constructor(): BookMapper {
    override fun Book.toBookEntity(): BookEntity {
        TODO("Not yet implemented")
    }

    override fun BookEntity.toBook(): Book {
        TODO("Not yet implemented")
    }
}