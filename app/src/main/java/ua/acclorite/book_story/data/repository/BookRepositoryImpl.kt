package ua.acclorite.book_story.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
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

    override suspend fun getBooks(query: String): List<Book> {
        val books = database.searchBooks(query)

        return books.map { entity ->
            val book = bookMapper.toBook(entity)
            val lastHistory = database.getLatestHistoryForBook(
                book.id
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
                book.id
            )

            book.copy(
                lastOpened = lastHistory?.time
            )
        }
    }

    override suspend fun getBookTextById(textPath: String): List<StringWithId> {
        val textFile = File(textPath)

        if (textPath.isBlank() || !textFile.exists()) {
            Log.w("BOOK TEXT", "Failed to load file")
            return emptyList()
        }

        val text = textParser.parse(textFile)

        if (text.data.isNullOrEmpty()) {
            return emptyList()
        }

        return text.data
    }

    override suspend fun insertBooks(
        books: List<Pair<Book, CoverImage?>>
    ): Boolean {
        val filesDir = application.filesDir
        val coversDir = File(filesDir, "covers")
        val booksDir = File(filesDir, "books")

        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }
        if (!booksDir.exists()) {
            booksDir.mkdirs()
        }

        val booksWithCoverAndText = books.map {
            var coverUri = ""
            val textUri: String

            if (it.first.text.isEmpty()) {
                return false
            }

            try {
                textUri = "${UUID.randomUUID()}.txt"
                val text = File(booksDir, textUri)

                withContext(Dispatchers.IO) {
                    FileOutputStream(text).use { stream ->
                        it.first.text.forEach { line ->
                            stream.write(line.line.toByteArray())
                            stream.write(System.lineSeparator().toByteArray())
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

            if (it.second != null) {
                try {
                    coverUri = "${UUID.randomUUID()}.webp"
                    val cover = File(coversDir, coverUri)

                    withContext(Dispatchers.IO) {
                        FileOutputStream(cover).use { stream ->
                            if (
                                !it.second!!.copy(Bitmap.Config.RGB_565, false).compress(
                                    Bitmap.CompressFormat.WEBP,
                                    20,
                                    stream
                                )
                            ) {
                                throw Exception("Couldn't save cover image")
                            }
                        }
                    }
                } catch (e: Exception) {
                    coverUri = ""
                    e.printStackTrace()
                }
            }


            val book = it.first.copy(
                textPath = "$booksDir/$textUri",
                coverImage = if (coverUri.isNotBlank()) {
                    Uri.fromFile(File("$coversDir/$coverUri"))
                } else null
            )

            bookMapper.toBookEntity(book)
        }

        database.insertBooks(booksWithCoverAndText)
        return true
    }

    override suspend fun updateBooks(books: List<Book>) {
        // without text and cover image
        database.updateBooks(
            books.map {
                val book = database.findBookById(it.id)
                bookMapper.toBookEntity(
                    it.copy(
                        textPath = book.textPath,
                        coverImage = if (book.image != null) Uri.parse(book.image) else null
                    )
                )
            }
        )
    }

    override suspend fun updateBooksWithText(books: List<Book>): Boolean {
        // without cover image
        val filesDir = application.filesDir
        val booksDir = File(filesDir, "books")

        if (!booksDir.exists()) {
            booksDir.mkdirs()
        }

        val booksWithText = books.map {
            val textUri: String
            val bookEntity = database.findBookById(it.id)

            if (it.text.isEmpty()) {
                return false
            }

            try {
                textUri = "${UUID.randomUUID()}.txt"
                val text = File(booksDir, textUri)

                withContext(Dispatchers.IO) {
                    FileOutputStream(text).use { stream ->
                        it.text.forEach { line ->
                            stream.write(line.line.toByteArray())
                            stream.write(System.lineSeparator().toByteArray())
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

            if (it.textPath.isNotBlank()) {
                try {
                    val fileToDelete = File(
                        it.textPath
                    )

                    if (fileToDelete.exists()) {
                        fileToDelete.delete()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            bookMapper.toBookEntity(
                it.copy(
                    textPath = "$booksDir/$textUri",
                    coverImage = if (bookEntity.image != null) Uri.parse(bookEntity.image) else null
                )
            )
        }

        database.updateBooks(
            booksWithText
        )
        return true
    }

    override suspend fun updateCoverImageOfBook(
        bookWithOldCover: Book,
        newCoverImage: CoverImage?
    ) {
        // without text
        val book = database.findBookById(bookWithOldCover.id)
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
                            !newCoverImage.copy(Bitmap.Config.RGB_565, false).compress(
                                Bitmap.CompressFormat.WEBP,
                                20,
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
            textPath = book.textPath,
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
        val booksDir = File(filesDir, "books")

        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }
        if (!booksDir.exists()) {
            booksDir.mkdirs()
        }

        database.deleteBooks(
            books.map {
                val book = database.findBookById(it.id)

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

                if (book.textPath.isNotBlank()) {
                    try {
                        val fileToDelete = File(book.textPath)

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
                        UIText.StringResource(R.string.error_something_went_wrong_with_file)
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















