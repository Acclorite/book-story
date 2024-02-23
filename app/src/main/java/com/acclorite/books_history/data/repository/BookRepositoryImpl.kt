package com.acclorite.books_history.data.repository

import android.os.Environment
import androidx.datastore.preferences.core.Preferences
import com.acclorite.books_history.data.local.data_store.DataStore
import com.acclorite.books_history.data.local.dto.BookEntity
import com.acclorite.books_history.data.local.room.BookDao
import com.acclorite.books_history.data.mapper.BookMapper
import com.acclorite.books_history.data.parser.EpubFileParser
import com.acclorite.books_history.data.parser.EpubTextParser
import com.acclorite.books_history.data.parser.PdfFileParser
import com.acclorite.books_history.data.parser.PdfTextParser
import com.acclorite.books_history.data.parser.TxtFileParser
import com.acclorite.books_history.data.parser.TxtTextParser
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.NullableBook
import com.acclorite.books_history.domain.model.StringWithId
import com.acclorite.books_history.domain.repository.BookRepository
import com.acclorite.books_history.util.Constants
import com.acclorite.books_history.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val database: BookDao,
    private val dataStore: DataStore,

    private val bookMapper: BookMapper,

    private val txtFileParser: TxtFileParser,
    private val pdfFileParser: PdfFileParser,
    private val epubFileParser: EpubFileParser,

    private val txtTextParser: TxtTextParser,
    private val pdfTextParser: PdfTextParser,
    private val epubTextParser: EpubTextParser,

    ) : BookRepository {

    override suspend fun getBooks(query: String): Flow<Resource<List<Book>>> {
        return flow {
            emit(Resource.Loading(true))

            val books = database.searchBooks(query)
            emit(
                Resource.Success(
                    data = books.map { bookMapper.toBook(it) }
                )
            )
        }
    }

    override suspend fun fastGetBooks(query: String): List<Book> {
        val books = database.searchBooks(query)
        return books.map { bookMapper.toBook(it) }
    }

    override suspend fun findBook(
        id: Int
    ): BookEntity {
        return database.findBookById(id)
    }

    override suspend fun insertBooks(books: List<Book>) {
        database.insertBooks(books.map { bookMapper.toBookEntity(it) })
    }

    override suspend fun updateBooks(books: List<Book>) {
        database.updateBooks(books.map { bookMapper.toBookEntity(it) })
    }

    override suspend fun deleteBooks(books: List<Book>) {
        database.deleteBooks(books.map { bookMapper.toBookEntity(it) })
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

    override suspend fun getFilesFromDownloads(query: String): Flow<Resource<List<File>>> {
        fun getAllFilesInDirectory(directory: File): List<File> {
            val filesList = mutableListOf<File>()

            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        filesList.add(file)
                    }
                    if (file.isDirectory) {
                        val subDirectoryFiles = getAllFilesInDirectory(file)
                        filesList.addAll(subDirectoryFiles)
                    }
                }
            }

            return filesList
        }

        return flow {
            emit(Resource.Loading(true))

            val existingBooks = database
                .searchBooks("")
                .map { bookMapper.toBook(it) }
                .filter { it.file != null }

            val allFiles = getAllFilesInDirectory(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            )

            if (allFiles.isEmpty()) {
                emit(Resource.Loading(false))
                return@flow
            }

            val supportedExtensions = Constants.EXTENSIONS
            var filteredFiles = mutableListOf<File>()

            allFiles.filter { file ->
                val isFileSupported = supportedExtensions.any { ext ->
                    file.name.endsWith(
                        ext,
                        ignoreCase = true
                    )
                }
                val isFileAlreadyAdded = existingBooks.all {
                    it.file != file
                }
                val isQuery = if (query.isEmpty()) true else file.name.lowercase()
                    .contains(query.trim().lowercase())

                isFileAlreadyAdded && isFileSupported && isQuery
            }.forEach {
                filteredFiles.add(
                    it
                )
            }

            filteredFiles = if (query.trim().isNotEmpty()) {
                filteredFiles.sortedByDescending {
                    it.name.lowercase().startsWith(query.trim().lowercase())
                }.toMutableList()
            } else {
                filteredFiles.sortedByDescending {
                    it.lastModified()
                }.toMutableList()
            }

            emit(Resource.Loading(false))
            emit(
                Resource.Success(
                    data = filteredFiles
                )
            )
        }
    }

    override suspend fun getBookTextFromFile(file: File): Flow<Resource<List<StringWithId>>> {
        return flow {
            emit(Resource.Loading(true))

            val text = if (file.name.endsWith(".txt")) {
                txtTextParser.parse(file)
            } else if (file.name.endsWith(".pdf")) {
                pdfTextParser.parse(file)
            } else if (file.name.endsWith(".epub")) {
                epubTextParser.parse(file)
            } else {
                return@flow
            }

            emit(Resource.Success(text))
        }
    }

    override suspend fun getBooksFromFiles(files: List<File>): Flow<Resource<List<NullableBook>>> {
        return flow {
            emit(Resource.Loading(true))

            val books = mutableListOf<NullableBook>()
            for (file in files) {

                val parsedBook = if (file.name.endsWith(".txt")) {
                    txtFileParser.parse(file)
                } else if (file.name.endsWith(".pdf")) {
                    pdfFileParser.parse(file)
                } else if (file.name.endsWith(".epub")) {
                    epubFileParser.parse(file)
                } else {
                    books.add(NullableBook.Null(file.name))
                    continue
                }

                val parsedText = if (file.name.endsWith(".txt")) {
                    txtTextParser.parse(file)
                } else if (file.name.endsWith(".pdf")) {
                    pdfTextParser.parse(file)
                } else if (file.name.endsWith(".epub")) {
                    epubTextParser.parse(file)
                } else {
                    books.add(NullableBook.Null(file.name))
                    continue
                }

                if (parsedBook == null || parsedText.isEmpty()) {
                    books.add(NullableBook.Null(file.name))
                    continue
                }

                books.add(
                    NullableBook.NotNull(
                        Pair(
                            parsedBook.copy(text = parsedText),
                            true
                        )
                    )
                )
            }

            emit(Resource.Success(books))
        }
    }
}














