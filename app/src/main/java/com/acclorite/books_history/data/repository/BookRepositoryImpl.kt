package com.acclorite.books_history.data.repository

import androidx.datastore.preferences.core.Preferences
import com.acclorite.books_history.data.local.data_store.DataStore
import com.acclorite.books_history.data.local.room.BookDao
import com.acclorite.books_history.data.mapper.BookMapper
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.repository.BookRepository
import com.acclorite.books_history.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val database: BookDao,
    private val dataStore: DataStore,

    private val bookMapper: BookMapper
) : BookRepository {

    override suspend fun getBooks(query: String): Flow<Resource<List<Book>>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertBooks(books: List<Book>) {
        database.insertBooks(books.map { bookMapper.toBookEntity(it) })
    }

    override suspend fun updateBooks(books: List<Book>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBooks(books: List<Book>) {
        TODO("Not yet implemented")
    }

    override suspend fun <T> retrieveDataFromDataStore(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return dataStore.getData(key, defaultValue)
    }

    override suspend fun <T> putDataToDataStore(key: Preferences.Key<T>, value: T) {
        dataStore.putData(key, value)
    }
}