package ua.acclorite.book_story.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.library.book.BookWithCover
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.CoverImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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

@Suppress("DEPRECATION")
@Singleton
class BookRepositoryImpl @Inject constructor(
    private val application: Application,
    private val database: BookDao,

    private val bookMapper: BookMapper,

    private val fileParser: FileParser,
    private val textParser: TextParser
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
     * Loads text from the book. Already formatted.
     */
    override suspend fun getBookText(bookId: Int): List<ReaderText> {
        if (bookId == -1) return emptyList()

        val book = database.findBookById(bookId)
        val file = File(book.filePath)

        if (!file.exists()) {
            Log.e(GET_TEXT, "File [$bookId] does not exist")
            return emptyList()
        }

        val readerText = textParser.parse(file)

        if (
            readerText.filterIsInstance<ReaderText.Text>().isEmpty() ||
            readerText.filterIsInstance<ReaderText.Chapter>().isEmpty()
        ) {
            Log.e(GET_TEXT, "Could not load text from [$bookId].")
            return emptyList()
        }

        Log.i(GET_TEXT, "Successfully loaded text of [$bookId] with markdown.")
        return readerText
    }

    /**
     * Inserts book in database.
     * Creates covers folder, which contains cover.
     */
    override suspend fun insertBook(
        bookWithCover: BookWithCover
    ): Boolean {
        Log.i(INSERT_BOOK, "Inserting ${bookWithCover.book.title}.")

        val filesDir = application.filesDir
        val coversDir = File(filesDir, "covers")

        if (!coversDir.exists()) {
            Log.i(INSERT_BOOK, "Created covers folder.")
            coversDir.mkdirs()
        }

        var coverUri = ""

        if (bookWithCover.coverImage != null) {
            try {
                coverUri = "${UUID.randomUUID()}.webp"
                val cover = File(coversDir, coverUri)

                withContext(Dispatchers.IO) {
                    FileOutputStream(cover).use { stream ->
                        if (
                            !bookWithCover.coverImage.copy(Bitmap.Config.RGB_565, false)
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

        val updatedBook = bookWithCover.book.copy(
            coverImage = if (coverUri.isNotBlank()) {
                Uri.fromFile(File("$coversDir/$coverUri"))
            } else null
        )

        val bookToInsert = bookMapper.toBookEntity(updatedBook)
        database.insertBook(bookToInsert)
        Log.i(INSERT_BOOK, "Successfully inserted book.")
        return true
    }

    /**
     * Update book without cover image.
     */
    override suspend fun updateBook(book: Book) {
        val entity = database.findBookById(book.id)
        database.updateBooks(
            listOf(
                bookMapper.toBookEntity(
                    book.copy(
                        coverImage = if (entity.image != null) Uri.parse(entity.image) else null
                    )
                )
            )
        )
    }

    /**
     * Update cover image of the book. Deletes old cover and replaces with new.
     */
    override suspend fun updateCoverImageOfBook(
        bookWithOldCover: Book,
        newCoverImage: CoverImage?
    ) {
        Log.i(UPDATE_BOOK, "Updating cover image: ${bookWithOldCover.title}.")

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

        if (!coversDir.exists()) {
            Log.i(DELETE_BOOKS, "Created covers folder.")
            coversDir.mkdirs()
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