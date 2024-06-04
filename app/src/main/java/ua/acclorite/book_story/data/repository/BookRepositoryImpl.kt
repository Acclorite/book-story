package ua.acclorite.book_story.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
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
import ua.acclorite.book_story.data.local.notification.UpdatesNotificationService
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.mapper.history.HistoryMapper
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.data.remote.GithubAPI
import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.DataStoreConstants
import ua.acclorite.book_story.domain.util.LanguageCode
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.data.MainState
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Suppress("DEPRECATION")
@Singleton
class BookRepositoryImpl @Inject constructor(
    private val application: Application,
    private val database: BookDao,
    private val dataStore: DataStore,
    private val modelManager: RemoteModelManager,
    private val languageIdentifier: LanguageIdentifier,

    private val githubAPI: GithubAPI,
    private val updatesNotificationService: UpdatesNotificationService,

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

        val enableTranslator = dataStore.getNullableData(DataStoreConstants.ENABLE_TRANSLATOR)
            ?: false

        val translateFrom = dataStore.getNullableData(DataStoreConstants.TRANSLATE_FROM)
            ?: "auto"

        val translateTo = dataStore.getNullableData(DataStoreConstants.TRANSLATE_TO) ?: if (
            Constants.LANGUAGES.any { Locale.getDefault().language.take(2) == it.first }
        ) {
            Locale.getDefault().language.take(2)
        } else {
            "en"
        }

        val doubleClickTranslation = dataStore.getNullableData(
            DataStoreConstants.DOUBLE_CLICK_TRANSLATION
        ) ?: true


        val updatedBook = book.copy(
            textPath = "$booksDir/$textUri",
            coverImage = if (coverUri.isNotBlank()) {
                Uri.fromFile(File("$coversDir/$coverUri"))
            } else null,
            enableTranslator = enableTranslator,
            translateFrom = translateFrom,
            translateTo = translateTo,
            doubleClickTranslation = doubleClickTranslation
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

                val isQuery = if (query.isEmpty()) true else file.name.trim().lowercase()
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

            filteredFiles = filteredFiles.sortedByDescending {
                it.lastModified()
            }.toMutableList()

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

    override suspend fun isLanguageModelDownloaded(languageCode: String): Boolean {
        val model = TranslateRemoteModel.Builder(languageCode).build()
        return suspendCoroutine { continuation ->
            modelManager.isModelDownloaded(model)
                .addOnSuccessListener {
                    continuation.resume(it)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }

    override suspend fun downloadLanguageModel(
        languageCode: String,
        onCompleted: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val isLanguageDownloaded = isLanguageModelDownloaded(languageCode)
        if (isLanguageDownloaded) {
            onCompleted()
            return
        }

        return suspendCoroutine { continuation ->
            val language = TranslateRemoteModel.Builder(languageCode).build()
            val conditions = DownloadConditions.Builder().build()

            modelManager.download(language, conditions)
                .addOnSuccessListener {
                    onCompleted()
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    onFailure(it)
                    onCompleted()
                    continuation.resume(Unit)
                }
                .addOnCanceledListener {
                    onCompleted()
                    Log.i("TRANSLATOR", "Downloading Model: Canceled")
                    continuation.resume(Unit)
                }
        }
    }

    override suspend fun deleteLanguageModel(languageCode: String): Boolean {
        val isLanguageDownloaded = isLanguageModelDownloaded(languageCode)
        if (!isLanguageDownloaded) {
            return true
        }

        return suspendCoroutine { continuation ->
            val language = TranslateRemoteModel.Builder(languageCode).build()

            modelManager.deleteDownloadedModel(language)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }

    override suspend fun identifyLanguage(text: String): Resource<String> {
        return suspendCoroutine { continuation ->
            languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener {
                    val languageCode = it.trim().lowercase().take(2)
                    var identified = true

                    if (languageCode == "ru") {
                        identified = false
                    }

                    if (TranslateLanguage.fromLanguageTag(languageCode) == null) {
                        identified = false
                    }

                    if (identified) {
                        continuation.resume(
                            Resource.Success(languageCode)
                        )
                        return@addOnSuccessListener
                    }

                    continuation.resume(
                        Resource.Error(UIText.StringResource(R.string.error_language_not_identified))
                    )
                }
                .addOnFailureListener {
                    continuation.resume(
                        Resource.Error(
                            UIText.StringResource(
                                R.string.error_query,
                                it.message ?: ""
                            )
                        )
                    )
                }
        }
    }

    override suspend fun translateText(
        sourceLanguage: LanguageCode,
        targetLanguage: LanguageCode,
        text: String
    ): Resource<String> {
        return suspendCoroutine { continuation ->
            val translatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage)
                .setTargetLanguage(targetLanguage)
                .build()
            val translator = Translation.getClient(translatorOptions)

            translator.translate(text)
                .addOnSuccessListener {
                    translator.close()
                    continuation.resume(
                        Resource.Success(
                            it.trim()
                        )
                    )
                }
                .addOnFailureListener {
                    translator.close()
                    continuation.resume(
                        Resource.Error(
                            UIText.StringResource(
                                R.string.error_query,
                                it.message ?: ""
                            )
                        )
                    )
                }
                .addOnCanceledListener {
                    translator.close()
                    continuation.resume(
                        Resource.Error(
                            UIText.StringResource(
                                R.string.error_something_went_wrong
                            )
                        )
                    )
                }
        }
    }
}















