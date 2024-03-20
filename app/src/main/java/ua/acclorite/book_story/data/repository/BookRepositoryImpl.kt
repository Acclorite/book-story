package ua.acclorite.book_story.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.local.data_store.DataStore
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.mapper.history.HistoryMapper
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.model.StringWithId
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.data.calculateFamiliarity
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val application: Application,
    private val database: BookDao,
    private val dataStore: DataStore,

    private val bookMapper: BookMapper,
    private val historyMapper: HistoryMapper,

    private val fileParser: FileParser,
    private val textParser: TextParser

) : BookRepository {

    override suspend fun getBooks(query: String): Flow<Resource<List<Book>>> {
        return flow {
            emit(Resource.Loading(true))
            val books = database.searchBooks(query)

            emit(
                Resource.Success(
                    data = books.map { entity ->
                        val book = bookMapper.toBook(entity)
                        val lastHistory = database.getLatestHistoryForBook(
                            book.id ?: -1
                        )

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

        return books.map { entity ->
            val book = bookMapper.toBook(entity)
            val lastHistory = database.getLatestHistoryForBook(
                book.id ?: -1
            )

            book.copy(
                lastOpened = lastHistory?.time
            )
        }
    }

    override suspend fun getBooksById(ids: List<Int>): List<Book> {
        val books = database.findBooksById(ids)

        return books.map { entity ->
            val book = bookMapper.toBook(entity)
            val lastHistory = database.getLatestHistoryForBook(
                book.id ?: -1
            )

            book.copy(
                lastOpened = lastHistory?.time
            )
        }
    }

    override suspend fun getBookTextById(bookId: Int): String {
        val book = database.findBookById(bookId)
        return book.text
    }

    override suspend fun insertBooks(
        books: List<Pair<Book, CoverImage?>>
    ) {
        val filesDir = application.filesDir
        val coversDir = File(filesDir, "covers")

        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }

        val booksWithCover = books.map {
            var uri = ""

            if (it.second != null) {
                try {
                    uri = "${UUID.randomUUID()}.webp"
                    val cover = File(coversDir, uri)

                    withContext(Dispatchers.IO) {
                        FileOutputStream(cover).use { stream ->
                            if (
                                !it.second!!.compress(
                                    Bitmap.CompressFormat.WEBP,
                                    30,
                                    stream
                                )
                            ) {
                                throw Exception("Couldn't save cover image")
                            }
                        }
                    }
                } catch (e: Exception) {
                    uri = ""
                    e.printStackTrace()
                }
            }

            val book = it.first.copy(
                coverImage = if (uri.isNotBlank()) {
                    Uri.fromFile(File("$coversDir/$uri"))
                } else null
            )

            bookMapper.toBookEntity(book)
        }

        database.insertBooks(booksWithCover)
    }

    override suspend fun updateBooks(books: List<Book>) {
        // without text and cover image
        database.updateBooks(
            books.map {
                val book = database.findBookById(it.id ?: return)
                bookMapper.toBookEntity(
                    it.copy(
                        text = book.text
                            .split("\n")
                            .map { line -> StringWithId(line.trim()) },
                        coverImage = if (book.image != null) Uri.parse(book.image) else null
                    )
                )
            }
        )
    }

    override suspend fun updateBooksWithText(books: List<Book>) {
        // without cover image
        database.updateBooks(
            books.map {
                val book = database.findBookById(it.id ?: return)
                bookMapper.toBookEntity(
                    it.copy(
                        coverImage = if (book.image != null) Uri.parse(book.image) else null
                    )
                )
            }
        )
    }

    override suspend fun updateCoverImageOfBook(
        bookWithOldCover: Book,
        newCoverImage: CoverImage?
    ) {
        // without text
        val book = database.findBookById(bookWithOldCover.id ?: return)
        var uri: String? = null

        val filesDir = application.filesDir
        val coversDir = File(filesDir, "covers")

        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }

        if (newCoverImage != null) {
            try {
                uri = "${UUID.randomUUID()}.webp"
                val cover = File(coversDir, uri)

                withContext(Dispatchers.IO) {
                    FileOutputStream(cover).use { stream ->
                        if (
                            !newCoverImage.compress(
                                Bitmap.CompressFormat.WEBP,
                                30,
                                stream
                            )
                        ) {
                            throw Exception("Couldn't save cover image")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
        }

        if (book.image != null) {
            try {
                val fileToDelete = File(
                    "$coversDir/${book.image.substringAfterLast("/")}"
                )

                if (fileToDelete.exists()) {
                    fileToDelete.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val newCoverImageUri = if (uri != null) {
            Uri.fromFile(File("$coversDir/$uri"))
        } else {
            null
        }

        val bookWithNewCover = bookWithOldCover.copy(
            text = book.text
                .split("\n")
                .map { line -> StringWithId(line.trim()) },
            coverImage = newCoverImageUri
        )

        database.updateBooks(
            listOf(
                bookMapper.toBookEntity(
                    bookWithNewCover
                )
            )
        )
    }

    override suspend fun deleteBooks(books: List<Book>) {
        val filesDir = application.filesDir
        val coversDir = File(filesDir, "covers")

        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }

        database.deleteBooks(
            books.map {
                val book = database.findBookById(it.id ?: return)

                if (book.image != null) {
                    try {
                        val fileToDelete = File(
                            "$coversDir/${book.image.substringAfterLast("/")}"
                        )

                        if (fileToDelete.exists()) {
                            fileToDelete.delete()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                book
            }
        )
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

    override suspend fun getFilesFromDevice(query: String): Flow<Resource<List<File>>> {
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
            val existingBooks = database
                .searchBooks("")
                .map { bookMapper.toBook(it) }

            val primaryDirectory = Environment.getExternalStorageDirectory()
            val allFiles = getAllFilesInDirectory(primaryDirectory)

            if (allFiles.isEmpty()) {
                emit(Resource.Success(null))
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

                if (!isFileSupported) {
                    return@filter false
                }

                val isFileNotAdded = existingBooks.all {
                    it.filePath.substringAfterLast("/") !=
                            file.path.substringAfterLast("/")
                }

                if (!isFileNotAdded) {
                    return@filter false
                }

                val isQuery = if (query.isEmpty()) true else file.name.lowercase()
                    .contains(query.trim().lowercase())

                if (!isQuery) {
                    return@filter false
                }

                true
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

            emit(
                Resource.Success(
                    data = filteredFiles
                )
            )
        }
    }

    override suspend fun getBooksFromFiles(files: List<File>): List<NullableBook> {
        val books = mutableListOf<NullableBook>()

        for (file in files) {
            val parsedBook = if (Constants.EXTENSIONS.any { file.name.endsWith(it) }) {
                fileParser.parse(file)
            } else {
                books.add(
                    NullableBook.Null(
                        file.name,
                        UIText.StringResource(R.string.error_wrong_file_format)
                    )
                )
                continue
            }

            val parsedText = if (Constants.EXTENSIONS.any { file.name.endsWith(it) }) {
                textParser.parse(file)
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
                        parsedBook.first.copy(text = parsedText.data),
                        parsedBook.second
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

    override suspend fun getLatestBookHistory(bookId: Int): History? {
        val history = database.getLatestHistoryForBook(bookId)
        return history?.let { historyMapper.toHistory(it) }
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















