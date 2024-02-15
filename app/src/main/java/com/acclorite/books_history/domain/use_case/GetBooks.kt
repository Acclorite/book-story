package com.acclorite.books_history.domain.use_case

import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.repository.BookRepository
import com.acclorite.books_history.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooks @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(query: String): Flow<Resource<List<Book>>> {
        return repository.getBooks(query)
    }
}