package ua.acclorite.book_story.domain.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.util.Resource
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


    suspend fun insertHistory(history: List<History>)

    suspend fun getHistory(): Flow<Resource<List<History>>>

    suspend fun deleteWholeHistory()

    suspend fun deleteBookHistory(bookId: Int)

    suspend fun deleteHistory(
        history: List<History>
    )
}