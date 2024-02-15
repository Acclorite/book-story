package com.acclorite.books_history.domain.use_case

import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.repository.BookRepository
import javax.inject.Inject

class FastGetBooks @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(query: String): List<Book> {
        return repository.fastGetBooks(query)
    }
}