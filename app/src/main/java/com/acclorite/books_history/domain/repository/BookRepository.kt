package com.acclorite.books_history.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.acclorite.books_history.data.local.dto.BookEntity
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.NullableBook
import com.acclorite.books_history.domain.model.StringWithId
import com.acclorite.books_history.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface BookRepository {

    suspend fun getBooks(
        query: String
    ): Flow<Resource<List<Book>>>

    suspend fun fastGetBooks(
        query: String
    ): List<Book>

    suspend fun findBook(
        id: Int
    ): BookEntity

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

    suspend fun getFilesFromDownloads(query: String = ""): Flow<Resource<List<File>>>

    suspend fun getBookTextFromFile(file: File): Flow<Resource<List<StringWithId>>>

    suspend fun getBooksFromFiles(files: List<File>): Flow<Resource<List<NullableBook>>>
}