package ua.acclorite.book_story.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.local.data_store.DataStore
import ua.acclorite.book_story.data.local.dto.FavoriteDirectoryEntity
import ua.acclorite.book_story.data.local.notification.UpdatesNotificationService
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.mapper.color_preset.ColorPresetMapper
import ua.acclorite.book_story.data.mapper.history.HistoryMapper
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.data.remote.GithubAPI
import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.BookWithText
import ua.acclorite.book_story.domain.model.BookWithTextAndCover
import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.model.ColorPreset
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.data.MainState
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val GET_BOOK_FROM_FILE = "GET BOOK FROM FILE, REPOSITORY"
private const val GET_TEXT = "GET TEXT, REPOSITORY"
private const val GET_BOOKS = "GET BOOKS, REPOSITORY"
private const val GET_BOOKS_BY_ID = "GET BOOKS, REPOSITORY"
private const val INSERT_BOOK = "INSERT BOOK, REPOSITORY"
private const val UPDATE_BOOK = "UPDATE BOOK, REPOSITORY"
private const val DELETE_BOOKS = "DELETE BOOKS, REPOSITORY"
private const val CAN_RESET_COVER = "CAN RESET COVER, REPOSITORY"
private const val RESET_COVER = "RESET COVER, REPOSITORY"
private const val GET_ALL_SETTINGS = "GET ALL SETTINGS, REPOSITORY"
private const val GET_FILES_FROM_DEVICE = "GET FILES FROM DEVICE, REPOSITORY"
private const val CHECK_FOR_UPDATES = "CHECK FOR UPDATES, REPOSITORY"

@Suppress("DEPRECATION")
@Singleton
class BookRepositoryImpl @Inject constructor(
    private val application: Application,
    private val database: BookDao,
    private val dataStore: DataStore,

    private val githubAPI: GithubAPI,
    private val updatesNotificationService: UpdatesNotificationService,

    private val bookMapper: BookMapper,
    private val historyMapper: HistoryMapper,
    private val colorPresetMapper: ColorPresetMapper,

    private val fileParser: FileParser,
    private val textParser: TextParser,
) : BookRepository {

    /**
     * Get all books matching [query] from database.
     * Empty [query] equals to all books.
     */
    override suspend fun getBooks(query: String): List<Book> {
        Log.i(GET_BOOKS, "Searching for books with query: \"$query\".")
        val books = database.searchBooks(query)

        Log.i(GET_BOOKS, "Found ${books.size} books.")
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

    /**
     * Get all books that match given [ids].
     */
    override suspend fun getBooksById(ids: List<Int>): List<Book> {
        Log.i(GET_BOOKS_BY_ID, "Getting books with ids: $ids.")
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

    /**
     * Loads text from given path. Should be .txt.
     * Used to get text from book and load Reader.
     */
    override suspend fun getBookText(textPath: String): List<String> {
        val textFile = File(textPath)
        val lines = mutableListOf<String>()

        if (textPath.isBlank() || !textFile.exists() || textFile.extension != "txt") {
            Log.w(GET_TEXT, "Failed to load file: $textPath")
            return emptyList()
        }

        withContext(Dispatchers.IO) {
            BufferedReader(FileReader(textFile)).forEachLine { line ->
                if (line.isNotBlank()) {
                    lines.add(
                        line.trim()
                    )
                }
            }
        }

        Log.i(GET_TEXT, "Successfully loaded text.")
        return lines
    }

    /**
     * Inserts book in database.
     * Creates covers and books folders, which contain book's text and cover.
     */
    override suspend fun insertBook(
        bookWithTextAndCover: BookWithTextAndCover
    ): Boolean {
        Log.i(INSERT_BOOK, "Inserting ${bookWithTextAndCover.book.title}.")

        val filesDir = application.filesDir
        val coversDir = File(filesDir, "covers")
        val booksDir = File(filesDir, "books")

        if (!coversDir.exists()) {
            Log.i(INSERT_BOOK, "Created covers folder.")
            coversDir.mkdirs()
        }
        if (!booksDir.exists()) {
            Log.i(INSERT_BOOK, "Created books folder.")
            booksDir.mkdirs()
        }

        var coverUri = ""
        val textUri: String

        if (bookWithTextAndCover.text.isEmpty()) {
            Log.e(INSERT_BOOK, "Text is empty.")
            return false
        }

        try {
            textUri = "${UUID.randomUUID()}.txt"
            val textPath = File(booksDir, textUri)

            withContext(Dispatchers.IO) {
                FileOutputStream(textPath).use { stream ->
                    bookWithTextAndCover.text.forEach { line ->
                        stream.write(line.toByteArray())
                        stream.write(System.lineSeparator().toByteArray())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(INSERT_BOOK, "Could not write text.")
            e.printStackTrace()
            return false
        }

        if (bookWithTextAndCover.coverImage != null) {
            try {
                coverUri = "${UUID.randomUUID()}.webp"
                val cover = File(coversDir, coverUri)

                withContext(Dispatchers.IO) {
                    FileOutputStream(cover).use { stream ->
                        if (
                            !bookWithTextAndCover.coverImage.copy(Bitmap.Config.RGB_565, false)
                                .compress(
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
                Log.e(INSERT_BOOK, "Could not save cover.")
                coverUri = ""
                e.printStackTrace()
            }
        }

        val updatedBook = bookWithTextAndCover.book.copy(
            textPath = "$booksDir/$textUri",
            coverImage = if (coverUri.isNotBlank()) {
                Uri.fromFile(File("$coversDir/$coverUri"))
            } else null
        )

        val bookToInsert = bookMapper.toBookEntity(updatedBook)
        database.insertBooks(listOf(bookToInsert))
        Log.i(INSERT_BOOK, "Successfully inserted book.")
        return true
    }

    /**
     * Update book without text or cover image.
     */
    override suspend fun updateBook(book: Book) {
        // without text and cover image
        val entity = database.findBookById(book.id)
        database.updateBooks(
            listOf(
                bookMapper.toBookEntity(
                    book.copy(
                        textPath = entity.textPath,
                        coverImage = if (entity.image != null) Uri.parse(entity.image) else null
                    )
                )
            )
        )
    }

    /**
     * Update book with text. Deletes old text file and replaces it with new.
     */
    override suspend fun updateBookWithText(bookWithText: BookWithText): Boolean {
        Log.i(UPDATE_BOOK, "Updating book with text: ${bookWithText.book.title}.")

        // without cover image
        val filesDir = application.filesDir
        val booksDir = File(filesDir, "books")

        if (!booksDir.exists()) {
            Log.i(UPDATE_BOOK, "Created books folder.")
            booksDir.mkdirs()
        }

        val textUri: String
        val bookEntity = database.findBookById(bookWithText.book.id)

        if (bookWithText.text.isEmpty()) {
            Log.e(UPDATE_BOOK, "Text is empty.")
            return false
        }

        try {
            textUri = "${UUID.randomUUID()}.txt"
            val textPath = File(booksDir, textUri)

            withContext(Dispatchers.IO) {
                FileOutputStream(textPath).use { stream ->
                    bookWithText.text.forEach { line ->
                        stream.write(line.toByteArray())
                        stream.write(System.lineSeparator().toByteArray())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(UPDATE_BOOK, "Could not update text.")
            e.printStackTrace()
            return false
        }

        if (bookWithText.book.textPath.isNotBlank()) {
            try {
                val fileToDelete = File(
                    bookWithText.book.textPath
                )

                if (fileToDelete.exists()) {
                    fileToDelete.delete()
                }
            } catch (e: Exception) {
                Log.e(UPDATE_BOOK, "Failed to delete old text.")
                e.printStackTrace()
            }
        }

        val updatedBook = bookMapper.toBookEntity(
            bookWithText.book.copy(
                textPath = "$booksDir/$textUri",
                coverImage = if (bookEntity.image != null) Uri.parse(bookEntity.image) else null
            )
        )

        database.updateBooks(
            listOf(updatedBook)
        )
        Log.i(UPDATE_BOOK, "Successfully updated book.")
        return true
    }

    /**
     * Update cover image of the book. Deletes old cover and replaces with new.
     */
    override suspend fun updateCoverImageOfBook(
        bookWithOldCover: Book,
        newCoverImage: CoverImage?
    ) {
        Log.i(UPDATE_BOOK, "Updating cover image: ${bookWithOldCover.title}.")

        // without text
        val book = database.findBookById(bookWithOldCover.id)
        var uri: String? = null

        val filesDir = application.filesDir
        val coversDir = File(filesDir, "covers")

        if (!coversDir.exists()) {
            Log.i(UPDATE_BOOK, "Created covers folder.")
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
                Log.e(UPDATE_BOOK, "Could not save new cover.")
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
                Log.e(UPDATE_BOOK, "Could not delete old cover.")
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
        Log.i(UPDATE_BOOK, "Successfully updated cover image.")
    }

    /**
     * Delete books.
     * Also deletes cover image and text from internal storage.
     */
    override suspend fun deleteBooks(books: List<Book>) {
        Log.i(DELETE_BOOKS, "Deleting books.")

        val filesDir = application.filesDir
        val coversDir = File(filesDir, "covers")
        val booksDir = File(filesDir, "books")

        if (!coversDir.exists()) {
            Log.i(DELETE_BOOKS, "Created covers folder.")
            coversDir.mkdirs()
        }
        if (!booksDir.exists()) {
            Log.i(DELETE_BOOKS, "Created books folder.")
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
                        Log.e(DELETE_BOOKS, "Could not delete cover image.")
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
                        Log.e(DELETE_BOOKS, "Could not delete text.")
                        e.printStackTrace()
                    }
                }

                book
            }
        )

        Log.i(DELETE_BOOKS, "Successfully deleted books.")
    }

    /**
     * @return Whether can reset cover image (restore default).
     */
    override suspend fun canResetCover(bookId: Int): Boolean {
        val book = database.findBookById(bookId)

        val defaultCoverUncompressed = fileParser.parse(File(book.filePath))?.coverImage
            ?: return false

        if (book.image == null) {
            Log.i(CAN_RESET_COVER, "Can reset cover image. (current is null)")
            return true
        }

        val stream = ByteArrayOutputStream()
        defaultCoverUncompressed.copy(Bitmap.Config.RGB_565, false).compress(
            Bitmap.CompressFormat.WEBP,
            20,
            stream
        )
        val byteArray = stream.toByteArray()
        val defaultCover = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        val currentCover = try {
            MediaStore.Images.Media.getBitmap(
                application.contentResolver,
                Uri.parse(book.image)
            )
        } catch (e: Exception) {
            Log.i(CAN_RESET_COVER, "Can reset cover image. (could not get current)")
            e.printStackTrace()
            return true
        }

        return !defaultCover.sameAs(currentCover)
    }

    /**
     * Reset cover image to default.
     * If there is no default cover, returns false.
     */
    override suspend fun resetCoverImage(bookId: Int): Boolean {
        if (!canResetCover(bookId)) {
            Log.w(RESET_COVER, "Cannot reset cover image.")
            return false
        }

        val book = database.findBookById(bookId)
        val defaultCover = fileParser.parse(File(book.filePath))?.coverImage
            ?: return false
        updateCoverImageOfBook(bookMapper.toBook(book), defaultCover)

        Log.i(RESET_COVER, "Successfully reset cover image.")
        return true
    }

    /**
     * Puts DataStore constant to [DataStore].
     */
    override suspend fun <T> putDataToDataStore(key: Preferences.Key<T>, value: T) {
        dataStore.putData(key, value)
    }

    /**
     * Gets all settings from DataStore and returns [MainState].
     */
    override suspend fun getAllSettings(): MainState {
        Log.i(GET_ALL_SETTINGS, "Getting all settings.")
        val result = CompletableDeferred<MainState>()

        withContext(Dispatchers.Default) {
            val keys = dataStore.getAllData()
            val data = mutableMapOf<String, Any>()

            Log.i(GET_ALL_SETTINGS, "Got ${keys?.size} settings keys.")

            val jobs = keys?.map { key ->
                async {
                    val nullableData = dataStore.getNullableData(key)

                    if (nullableData == null) {
                        data.remove(key.name)
                    } else {
                        data[key.name] = nullableData
                    }
                }
            }
            jobs?.awaitAll()

            result.complete(MainState.initialize(data))
        }

        return result.await()
    }

    /**
     * Get all matching files from device.
     * Filters by [query] and sorts out not supported file formats and already added files.
     */
    override suspend fun getFilesFromDevice(query: String): List<SelectableFile> {
        Log.i(GET_FILES_FROM_DEVICE, "Getting files from device by query: \"$query\".")

        val existingBooks = database
            .searchBooks("")
            .map { bookMapper.toBook(it) }
        val supportedExtensions = Constants.EXTENSIONS

        fun File.isValid(): Boolean {
            if (!exists()) {
                return false
            }

            val isFileSupported = supportedExtensions.any { ext ->
                name.endsWith(
                    ext,
                    ignoreCase = true
                )
            }

            if (!isFileSupported) {
                return false
            }

            val isFileNotAdded = existingBooks.all {
                it.filePath.lowercase().trim() != path.lowercase().trim()
            }

            if (!isFileNotAdded) {
                return false
            }

            val isQuery = if (query.isEmpty()) true else name.trim().lowercase()
                .contains(query.trim().lowercase())

            return isQuery
        }

        suspend fun File.getAllFiles(): List<SelectableFile> {
            val filesList = mutableListOf<SelectableFile>()

            val files = listFiles()
            if (files != null) {
                for (file in files) {
                    if (!file.exists()) {
                        continue
                    }

                    when {
                        file.isFile -> {
                            if (file.isValid()) {
                                filesList.add(
                                    SelectableFile(
                                        fileOrDirectory = file,
                                        parentDirectory = this,
                                        isDirectory = false,
                                        isFavorite = false,
                                        isSelected = false
                                    )
                                )
                            }
                        }

                        file.isDirectory -> {
                            val subDirectoryFiles = file.getAllFiles()
                            if (subDirectoryFiles.isNotEmpty()) {
                                filesList.add(
                                    SelectableFile(
                                        fileOrDirectory = file,
                                        parentDirectory = this,
                                        isDirectory = true,
                                        isFavorite = database.favoriteDirectoryExits(file.path),
                                        isSelected = false
                                    )
                                )
                                filesList.addAll(subDirectoryFiles)
                            }
                        }
                    }
                }
            }

            return filesList
        }

        val rootDirectory = Environment.getExternalStorageDirectory()
        if (
            !rootDirectory.exists() ||
            !rootDirectory.isDirectory ||
            (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED &&
                    Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED_READ_ONLY)
        ) {
            Log.e(GET_FILES_FROM_DEVICE, "Could not correctly get root directory.")
            return emptyList()
        }

        Log.i(GET_FILES_FROM_DEVICE, "Successfully got all matching files.")
        return rootDirectory.getAllFiles()
    }

    /**
     * Gets book from given file. If error happened, returns [NullableBook.Null].
     */
    override suspend fun getBookFromFile(file: File): NullableBook {
        val parsedBook = fileParser.parse(file)
        if (parsedBook == null) {
            Log.e(GET_BOOK_FROM_FILE, "Parsed file(${file.name}) is null.")
            return NullableBook.Null(
                file.name,
                UIText.StringResource(R.string.error_something_went_wrong)
            )
        }

        val parsedText = textParser.parse(file)
        if (parsedText is Resource.Error) {
            Log.e(GET_BOOK_FROM_FILE, "Parsed text(${file.name}) has error.")
            return NullableBook.Null(
                file.name,
                parsedText.message
            )
        }

        Log.i(GET_BOOK_FROM_FILE, "Successfully got book from file.")
        return NullableBook.NotNull(
            bookWithTextAndCover = BookWithTextAndCover(
                book = parsedBook.book.copy(
                    chapters = parsedText.data!!.map { it.chapter }.run {
                        if (this.size == 1) return@run emptyList()
                        this
                    }
                ),
                coverImage = parsedBook.coverImage,
                text = parsedText.data.map {
                    it.text
                }.flatten()
            )
        )
    }

    /**
     * Parse text from given [file].
     * May give [Resource.Error] if something went wrong.
     *
     * @param file File to parse. Should be one of supported file formats.
     */
    override suspend fun parseText(file: File): Resource<List<ChapterWithText>> {
        return textParser.parse(file)
    }

    /**
     * Insert history in database.
     */
    override suspend fun insertHistory(history: History) {
        database.insertHistory(
            listOf(
                historyMapper.toHistoryEntity(
                    history
                )
            )
        )
    }

    /**
     * Get all history from database.
     */
    override suspend fun getHistory(): List<History> {
        return database.getHistory().map {
            historyMapper.toHistory(
                it
            )
        }
    }

    /**
     * Get latest history of the matching [bookId].
     */
    override suspend fun getLatestBookHistory(bookId: Int): History? {
        val history = database.getLatestHistoryForBook(bookId)
        return history?.let { historyMapper.toHistory(it) }
    }

    /**
     * Delete whole history.
     */
    override suspend fun deleteWholeHistory() {
        database.deleteWholeHistory()
    }

    /**
     * Delete all history of the matching [bookId].
     */
    override suspend fun deleteBookHistory(bookId: Int) {
        database.deleteBookHistory(bookId)
    }

    /**
     * Delete specific history item.
     */
    override suspend fun deleteHistory(history: History) {
        database.deleteHistory(
            listOf(
                historyMapper.toHistoryEntity(
                    history
                )
            )
        )
    }

    /**
     * Check for updates from GitHub.
     *
     * @param postNotification Whether notification should be send.
     */
    override suspend fun checkForUpdates(postNotification: Boolean): LatestReleaseInfo? {
        Log.i(CHECK_FOR_UPDATES, "Checking for updates. Post notification: $postNotification")

        return withContext(Dispatchers.IO) {
            try {
                val result = githubAPI.getLatestRelease()

                val version = result.tagName.substringAfter("v")
                val currentVersion = application.getString(R.string.app_version)

                if (version != currentVersion && postNotification) {
                    Log.i(CHECK_FOR_UPDATES, "Posting notification.")
                    updatesNotificationService.postNotification(
                        result
                    )
                }

                result
            } catch (e: Exception) {
                Log.e(CHECK_FOR_UPDATES, "Could not get latest release information.")
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Update color preset.
     */
    override suspend fun updateColorPreset(colorPreset: ColorPreset) {
        database.updateColorPreset(
            colorPresetMapper.toColorPresetEntity(
                colorPreset,
                if (colorPreset.id != -1) database.getColorPresetOrder(colorPreset.id)
                else database.getColorPresetsSize()
            )
        )
    }

    /**
     * Select color preset. Only one can be selected at time.
     */
    override suspend fun selectColorPreset(colorPreset: ColorPreset) {
        database.getColorPresets().map {
            it.copy(
                isSelected = it.id == colorPreset.id
            )
        }.forEach {
            database.updateColorPreset(it)
        }
    }

    /**
     * Get all color presets.
     * Sorted by order (either manual or newest ones at the end).
     */
    override suspend fun getColorPresets(): List<ColorPreset> {
        return database.getColorPresets()
            .sortedBy { it.order }
            .map { colorPresetMapper.toColorPreset(it) }
    }

    /**
     * Reorder color presets.
     * Changes the order of the color presets.
     */
    override suspend fun reorderColorPresets(orderedColorPresets: List<ColorPreset>) {
        database.deleteColorPresets()

        orderedColorPresets.forEachIndexed { index, colorPreset ->
            database.updateColorPreset(
                colorPresetMapper.toColorPresetEntity(colorPreset, order = index)
            )
        }
    }

    /**
     * Delete color preset.
     */
    override suspend fun deleteColorPreset(colorPreset: ColorPreset) {
        database.deleteColorPreset(
            colorPresetMapper.toColorPresetEntity(
                colorPreset, -1
            )
        )
    }

    /**
     * Create or delete favorite directory if already exists.
     *
     * @param path Path to directory.
     */
    override suspend fun updateFavoriteDirectory(path: String) {
        if (database.favoriteDirectoryExits(path)) {
            database.deleteFavoriteDirectory(
                FavoriteDirectoryEntity(path)
            )
            return
        }

        database.insertFavoriteDirectory(
            FavoriteDirectoryEntity(path)
        )
    }
}