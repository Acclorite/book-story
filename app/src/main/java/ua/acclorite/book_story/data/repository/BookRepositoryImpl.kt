package ua.acclorite.book_story.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.data.parser.MarkdownParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.BookWithText
import ua.acclorite.book_story.domain.model.BookWithTextAndCover
import ua.acclorite.book_story.domain.model.Chapter
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val GET_TEXT = "GET TEXT, REPO"
private const val GET_BOOKS = "GET BOOKS, REPO"
private const val GET_BOOKS_BY_ID = "GET BOOKS, REPO"
private const val INSERT_BOOK = "INSERT BOOK, REPO"
private const val UPDATE_BOOK = "UPDATE BOOK, REPO"
private const val DELETE_BOOKS = "DELETE BOOKS, REPO"
private const val CAN_RESET_COVER = "CAN RESET COVER, REPO"
private const val RESET_COVER = "RESET COVER, REPO"
private const val CHECK_FOR_TEXT_UPDATE = "CHECK TEXT UPD, REPO"

@Suppress("DEPRECATION")
@Singleton
class BookRepositoryImpl @Inject constructor(
    private val application: Application,
    private val database: BookDao,

    private val bookMapper: BookMapper,

    private val fileParser: FileParser,
    private val textParser: TextParser,
    private val markdownParser: MarkdownParser
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
    override suspend fun getBookText(textPath: String): List<AnnotatedString> {
        val textFile = File(textPath)
        val markdownLines = mutableListOf<AnnotatedString>()

        if (textPath.isBlank() || !textFile.exists() || textFile.extension != "txt") {
            Log.w(GET_TEXT, "Failed to load file: $textPath")
            return emptyList()
        }

        try {
            withContext(Dispatchers.IO) {
                BufferedReader(FileReader(textFile)).forEachLine { line ->
                    if (line.isNotBlank()) {
                        markdownLines.add(
                            markdownParser.parse(line.trim())
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(GET_TEXT, "Could not get text with markdown.")
            return emptyList()
        }

        Log.i(GET_TEXT, "Successfully loaded text with markdown.")
        return markdownLines
    }

    /**
     * Checks whether the text of the book([bookId]) is up-to-date and did not change.
     *
     * @return If [Resource.Success] and returns null, then the book is up-to-date.
     */
    override suspend fun checkForTextUpdate(bookId: Int): Resource<Pair<List<String>, List<Chapter>>?> {
        yield()

        try {
            val book = database.findBookById(bookId)
            Log.i(CHECK_FOR_TEXT_UPDATE, "Checking [${book.title}] for text update.")

            yield()

            val text = withContext(Dispatchers.IO) {
                val lines = mutableListOf<String>()
                BufferedReader(FileReader(book.textPath)).forEachLine { line ->
                    if (line.isNotBlank()) {
                        lines.add(line.trim())
                    }
                }
                lines.toList()
            }
            val chapters = book.chapters
            Log.i(CHECK_FOR_TEXT_UPDATE, "Got current text and chapters.")

            yield()

            val bookFile = File(book.filePath).apply {
                if (!exists()) {
                    Log.e(CHECK_FOR_TEXT_UPDATE, "Couldn't get book's file: does not exist.")
                    return Resource.Error(
                        message = UIText.StringResource(
                            R.string.file_not_found,
                            name.takeLast(50)
                        )
                    )
                }
            }

            yield()

            val (updatedText, updatedChapters) = textParser.parse(bookFile).run {
                if (this is Resource.Error) {
                    Log.e(CHECK_FOR_TEXT_UPDATE, "Couldn't get updated book's text.")
                    return Resource.Error(
                        message = UIText.StringResource(
                            R.string.error_file_empty
                        )
                    )
                }

                data!!.map { it.text }.flatten() to data.map { it.chapter }
            }
            Log.i(CHECK_FOR_TEXT_UPDATE, "Successfully got new text and chapters.")

            yield()

            return Resource.Success(
                if (updatedText == text && updatedChapters == chapters) {
                    Log.i(CHECK_FOR_TEXT_UPDATE, "Text is up-to-date(${book.title}).")
                    null
                } else {
                    Log.i(CHECK_FOR_TEXT_UPDATE, "Found difference in ${book.title}.")
                    updatedText to updatedChapters
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(CHECK_FOR_TEXT_UPDATE, "Check failed with the error: ${e.message}")
            return Resource.Error(
                UIText.StringResource(
                    R.string.error_query,
                    e.message ?: ""
                )
            )
        }
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
}