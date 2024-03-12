package ua.acclorite.book_story.data.repository

import android.os.Environment
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.local.data_store.DataStore
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.mapper.history.HistoryMapper
import ua.acclorite.book_story.data.parser.epub.EpubFileParser
import ua.acclorite.book_story.data.parser.epub.EpubTextParser
import ua.acclorite.book_story.data.parser.pdf.PdfFileParser
import ua.acclorite.book_story.data.parser.pdf.PdfTextParser
import ua.acclorite.book_story.data.parser.txt.TxtFileParser
import ua.acclorite.book_story.data.parser.txt.TxtTextParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.presentation.data.calculateFamiliarity
import ua.acclorite.book_story.util.Constants
import ua.acclorite.book_story.util.Resource
import ua.acclorite.book_story.util.UIText
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
            val history = database.getHistory()

            emit(
                Resource.Success(
                    data = books.map { entity ->
                        val book = bookMapper.toBook(entity)
                        val lastHistory = history
                            .filter { it.bookId == book.id }
                            .maxByOrNull { it.time }

                        book.copy(
                            lastOpened = lastHistory?.time
                        )
                    }
                )
            )
        }
    }

    override suspend fun fastGetBooks(query: String): List<Book> {
        val books = database.searchBooks(query)
        val history = database.getHistory()

        return books.map { entity ->
            val book = bookMapper.toBook(entity)
            val lastHistory =
                history.filter { it.bookId == book.id }.maxByOrNull { it.time }

            book.copy(
                lastOpened = lastHistory?.time
            )
        }
    }

    override suspend fun getBooksById(ids: List<Int>): List<Book> {
        return database.findBooksById(ids).map { bookMapper.toBook(it) }
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
                    calculateFamiliarity(
                        query,
                        it.name.lowercase().trim()
                    )
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
                emit(Resource.Error(UIText.StringResource(R.string.error_wrong_file_format)))
                return@flow
            }

            if (text is Resource.Success && text.data == null) {
                emit(
                    Resource.Error(
                        text.message
                            ?: UIText.StringResource(R.string.error_file_empty)
                    )
                )
            }

            when (text) {
                is Resource.Success -> emit(Resource.Success(text.data))
                is Resource.Error -> emit(
                    Resource.Error(
                        text.message
                            ?: UIText.StringResource(R.string.error_something_went_wrong)
                    )
                )

                is Resource.Loading -> Unit
            }

        }
    }

    override suspend fun getBooksFromFiles(files: List<File>): List<NullableBook> {
        val books = mutableListOf<NullableBook>()

        for (file in files) {
            val parsedBook = if (file.name.endsWith(".txt")) {
                txtFileParser.parse(file)
            } else if (file.name.endsWith(".pdf")) {
                pdfFileParser.parse(file)
            } else if (file.name.endsWith(".epub")) {
                epubFileParser.parse(file)
            } else {
                books.add(
                    NullableBook.Null(
                        file.name,
                        UIText.StringResource(R.string.error_wrong_file_format)
                    )
                )
                continue
            }

            val parsedText = if (file.name.endsWith(".txt")) {
                txtTextParser.parse(file)
            } else if (file.name.endsWith(".pdf")) {
                pdfTextParser.parse(file)
            } else if (file.name.endsWith(".epub")) {
                epubTextParser.parse(file)
            } else {
                books.add(
                    NullableBook.Null(
                        file.name,
                        UIText.StringResource(R.string.error_wrong_file_format)
                    )
                )
                continue
            }

            if (parsedBook == null) {
                books.add(
                    NullableBook.Null(
                        file.name,
                        UIText.StringResource(R.string.error_something_went_wrong)
                    )
                )
                continue
            }

            if (parsedText is Resource.Error) {
                books.add(
                    NullableBook.Null(
                        file.name,
                        parsedText.message
                    )
                )
                continue
            }

            if (parsedText.data == null) {
                books.add(
                    NullableBook.Null(
                        file.name,
                        UIText.StringResource(R.string.error_file_empty)
                    )
                )
                continue
            }

            books.add(
                NullableBook.NotNull(
                    Pair(
                        parsedBook.copy(text = parsedText.data),
                        true
                    )
                )
            )
        }

        return books
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
                        historyMapper.toHistory(
                            it
                        )
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















