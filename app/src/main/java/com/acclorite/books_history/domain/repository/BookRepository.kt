package com.acclorite.books_history.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.util.Resource
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    suspend fun getBooks(
        query: String
    ): Flow<Resource<List<Book>>>

    suspend fun insertBooks(
        books: List<Book>
    )

    suspend fun updateBooks(
        books: List<Book>
    )

    suspend fun deleteBooks(
        books: List<Book>
    )

    suspend fun <T> retrieveDataFromDataStore(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T>

    suspend fun <T> putDataToDataStore(
        key: Preferences.Key<T>,
        value: T
    )
}