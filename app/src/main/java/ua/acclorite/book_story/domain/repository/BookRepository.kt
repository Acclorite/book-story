package ua.acclorite.book_story.domain.repository

import androidx.compose.ui.text.AnnotatedString
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.library.book.BookWithText
import ua.acclorite.book_story.domain.library.book.BookWithTextAndCover
import ua.acclorite.book_story.domain.reader.Chapter
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.Resource

interface BookRepository {

    suspend fun getBooks(
        query: String
    ): List<Book>

    suspend fun getBooksById(
        ids: List<Int>
    ): List<Book>

    suspend fun getBookText(
        textPath: String
    ): List<AnnotatedString>

    suspend fun checkForTextUpdate(bookId: Int): Resource<Pair<List<String>, List<Chapter>>?>

    suspend fun insertBook(
        bookWithTextAndCover: BookWithTextAndCover
    ): Boolean

    suspend fun updateBook(
        book: Book
    )

    suspend fun updateBookWithText(
        bookWithText: BookWithText
    ): Boolean

    suspend fun updateCoverImageOfBook(
        bookWithOldCover: Book,
        newCoverImage: CoverImage?
    )

    suspend fun deleteBooks(
        books: List<Book>
    )

    suspend fun canResetCover(
        bookId: Int
    ): Boolean

    suspend fun resetCoverImage(
        bookId: Int
    ): Boolean
}