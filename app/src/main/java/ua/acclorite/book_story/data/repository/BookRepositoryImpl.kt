package ua.acclorite.book_story.data.repository

import android.os.Environment
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.acclorite.book_story.data.local.data_store.DataStore
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.BookMapper
import ua.acclorite.book_story.data.mapper.HistoryMapper
import ua.acclorite.book_story.data.parser.EpubFileParser
import ua.acclorite.book_story.data.parser.EpubTextParser
import ua.acclorite.book_story.data.parser.PdfFileParser
import ua.acclorite.book_story.data.parser.PdfTextParser
import ua.acclorite.book_story.data.parser.TxtFileParser
import ua.acclorite.book_story.data.parser.TxtTextParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.util.Constants
import ua.acclorite.book_story.util.Resource
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val database: BookDao,
    private val dataStore: DataStore,

    private val bookMapper: BookMapper,
    private val historyMapper: HistoryMapper,

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

    override suspend fun insertHistory(history: List<History>) {
        database.insertHistory(history.map { historyMapper.toHistoryEntity(it) })
    }

    override suspend fun getHistory(): Flow<Resource<List<History>>> {
        return flow {
            emit(Resource.Loading(true))

            val history = database.getHistory()
            emit(
                Resource.Success(
                    data = history.map {
                        val book = database.findBookById(it.bookId)
                        historyMapper.toHistory(it, bookMapper.toBook(book))
                    }
                )
            )
        }
    }

    override suspend fun deleteWholeHistory() {
        database.deleteWholeHistory()
    }

    override suspend fun deleteBookHistory(bookId: Int) {
        database.deleteBookHistory(bookId)
    }

    override suspend fun deleteHistory(history: List<History>) {
        database.deleteHistory(history.map { historyMapper.toHistoryEntity(it) })
    }
}















