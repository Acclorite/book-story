package com.acclorite.books_history.domain.use_case

import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.repository.BookRepository
import javax.inject.Inject

class UpdateBooks @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(books: List<Book>) {
        repository.updateBooks(books)
    }
}