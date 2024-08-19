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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
import ua.acclorite.book_story.domain.model.ColorPreset
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.data.MainState
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

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

    override suspend fun getBookText(textPath: String): List<String> {
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

    override suspend fun insertBook(
        book: Book,
        coverImage: CoverImage?,
        text: List<String>
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

        var coverUri = ""
        val textUri: String

        if (text.isEmpty()) {
            return false
        }

        try {
            textUri = "${UUID.randomUUID()}.txt"
            val textPath = File(booksDir, textUri)

            withContext(Dispatchers.IO) {
                FileOutputStream(textPath).use { stream ->
                    text.forEach { line ->
                        stream.write(line.toByteArray())
                        stream.write(System.lineSeparator().toByteArray())
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        if (coverImage != null) {
            try {
                coverUri = "${UUID.randomUUID()}.webp"
                val cover = File(coversDir, coverUri)

                withContext(Dispatchers.IO) {
                    FileOutputStream(cover).use { stream ->
                        if (
                            !coverImage.copy(Bitmap.Config.RGB_565, false).compress(
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

        val updatedBook = book.copy(
            textPath = "$booksDir/$textUri",
            coverImage = if (coverUri.isNotBlank()) {
                Uri.fromFile(File("$coversDir/$coverUri"))
            } else null
        )

        val bookToInsert = bookMapper.toBookEntity(updatedBook)
        database.insertBooks(listOf(bookToInsert))
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

    override suspend fun updateBookWithText(book: Book, text: List<String>): Boolean {
        // without cover image
        val filesDir = application.filesDir
        val booksDir = File(filesDir, "books")

        if (!booksDir.exists()) {
            booksDir.mkdirs()
        }

        val textUri: String
        val bookEntity = database.findBookById(book.id)

        if (text.isEmpty()) {
            return false
        }

        try {
            textUri = "${UUID.randomUUID()}.txt"
            val textPath = File(booksDir, textUri)

            withContext(Dispatchers.IO) {
                FileOutputStream(textPath).use { stream ->
                    text.forEach { line ->
                        stream.write(line.toByteArray())
                        stream.write(System.lineSeparator().toByteArray())
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        if (book.textPath.isNotBlank()) {
            try {
                val fileToDelete = File(
                    book.textPath
                )

                if (fileToDelete.exists()) {
                    fileToDelete.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val updatedBook = bookMapper.toBookEntity(
            book.copy(
                textPath = "$booksDir/$textUri",
                coverImage = if (bookEntity.image != null) Uri.parse(bookEntity.image) else null
            )
        )

        database.updateBooks(
            listOf(updatedBook)
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

    override suspend fun canResetCover(bookId: Int): Boolean {
        val book = database.findBookById(bookId)

        val defaultCoverUncompressed = fileParser.parse(File(book.filePath))?.second
            ?: return false

        if (book.image == null) {
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
            e.printStackTrace()
            return true
        }

        return !defaultCover.sameAs(currentCover)
    }

    override suspend fun resetCoverImage(bookId: Int): Boolean {
        if (!canResetCover(bookId)) {
            return false
        }

        val book = database.findBookById(bookId)
        val defaultCover = fileParser.parse(File(book.filePath))?.second
            ?: return false
        updateCoverImageOfBook(bookMapper.toBook(book), defaultCover)

        return true
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

    override suspend fun getAllSettings(scope: CoroutineScope): MainState {
        val result = CompletableDeferred<MainState>()

        scope.launch {
            val keys = dataStore.getAllData()
            val data = mutableMapOf<String, Any>()

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

    override suspend fun getFilesFromDevice(query: String): List<SelectableFile> {
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
            return emptyList()
        }

        return rootDirectory.getAllFiles()
    }

    override suspend fun getBooksFromFiles(files: List<File>): List<NullableBook> {
        val books = mutableListOf<NullableBook>()

        for (file in files) {
            val parsedBook = if (Constants.EXTENSIONS.any { file.name.endsWith(it, true) }) {
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

            val parsedText = if (Constants.EXTENSIONS.any { file.name.endsWith(it, true) }) {
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
                    book = parsedBook.first,
                    coverImage = parsedBook.second,
                    text = parsedText.data
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

    override suspend fun checkForUpdates(postNotification: Boolean): LatestReleaseInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val result = githubAPI.getLatestRelease()

                val version = result.tagName.substringAfter("v")
                val currentVersion = application.getString(R.string.app_version)

                if (version != currentVersion && postNotification) {
                    updatesNotificationService.postNotification(
                        result
                    )
                }

                result
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun updateColorPreset(colorPreset: ColorPreset) {
        database.updateColorPreset(
            colorPresetMapper.toColorPresetEntity(
                colorPreset,
                if (colorPreset.id != -1) database.getColorPresetOrder(colorPreset.id)
                else database.getColorPresetsSize()
            )
        )
    }

    override suspend fun selectColorPreset(colorPreset: ColorPreset) {
        database.getColorPresets().map {
            it.copy(
                isSelected = it.id == colorPreset.id
            )
        }.forEach {
            database.updateColorPreset(it)
        }
    }

    override suspend fun getColorPresets(): List<ColorPreset> {
        return database.getColorPresets()
            .sortedBy { it.order }
            .map { colorPresetMapper.toColorPreset(it) }
    }

    override suspend fun reorderColorPresets(orderedColorPresets: List<ColorPreset>) {
        database.deleteColorPresets()

        orderedColorPresets.forEachIndexed { index, colorPreset ->
            database.updateColorPreset(
                colorPresetMapper.toColorPresetEntity(colorPreset, order = index)
            )
        }
    }

    override suspend fun deleteColorPreset(colorPreset: ColorPreset) {
        database.deleteColorPreset(
            colorPresetMapper.toColorPresetEntity(
                colorPreset, -1
            )
        )
    }

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















