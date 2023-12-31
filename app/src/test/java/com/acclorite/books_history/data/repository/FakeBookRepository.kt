package com.acclorite.books_history.data.repository

import androidx.datastore.preferences.core.Preferences
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.repository.BookRepository
import com.acclorite.books_history.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBookRepository: BookRepository {

    private val booksList = mutableListOf<Book>()

    override suspend fun getBooks(query: String): Flow<Resource<List<Book>>> {
        return flow { emit(Resource.Success(booksList.filter { it.title.lowercase().contains(query) })) }
    }

    override suspend fun insertBooks(books: List<Book>) {
        booksList.addAll(books)
    }

    override suspend fun updateBooks(books: List<Book>) {
        booksList.forEachIndexed { index, book ->
            books.forEach {
                if(it.id == book.id)
                    booksList[index] = it
            }
        }
    }

    override suspend fun deleteBooks(books: List<Book>) {
        booksList.removeAll(books)
    }

    override suspend fun <T> retrieveDataFromDataStore(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return flow { emit(defaultValue) }
    }

    override suspend fun <T> putDataToDataStore(key: Preferences.Key<T>, value: T) {
        TODO("Not yet implemented")
    }
}