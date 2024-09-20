package ua.acclorite.book_story.domain.repository

import androidx.datastore.preferences.core.Preferences
import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.BookWithText
import ua.acclorite.book_story.domain.model.BookWithTextAndCover
import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.model.ColorPreset
import ua.acclorite.book_story.domain.model.History
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.presentation.data.MainState
import java.io.File

interface BookRepository {

    /* ------ Books ------------------------------ */
    suspend fun getBooks(
        query: String
    ): List<Book>

    suspend fun getBooksById(
        ids: List<Int>
    ): List<Book>

    suspend fun getBookText(
        textPath: String
    ): List<String>

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
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ DataStore -------------------------- */
    suspend fun <T> putDataToDataStore(
        key: Preferences.Key<T>,
        value: T
    )

    suspend fun getAllSettings(): MainState
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ File System ------------------------ */
    suspend fun getFilesFromDevice(
        query: String = ""
    ): List<SelectableFile>

    suspend fun getBookFromFile(
        file: File
    ): NullableBook

    suspend fun parseText(file: File): Resource<List<ChapterWithText>>
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ History ---------------------------- */
    suspend fun insertHistory(
        history: History
    )

    suspend fun getHistory(): List<History>

    suspend fun getLatestBookHistory(
        bookId: Int
    ): History?

    suspend fun deleteWholeHistory()

    suspend fun deleteBookHistory(
        bookId: Int
    )

    suspend fun deleteHistory(
        history: History
    )
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ API (GitHub) ----------------------- */
    suspend fun checkForUpdates(
        postNotification: Boolean
    ): LatestReleaseInfo?
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ Color Presets ---------------------- */
    suspend fun updateColorPreset(
        colorPreset: ColorPreset
    )

    suspend fun selectColorPreset(
        colorPreset: ColorPreset
    )

    suspend fun getColorPresets(): List<ColorPreset>

    suspend fun reorderColorPresets(
        orderedColorPresets: List<ColorPreset>
    )

    suspend fun deleteColorPreset(
        colorPreset: ColorPreset
    )
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ Favorite Directories --------------- */
    suspend fun updateFavoriteDirectory(path: String)
    /* - - - - - - - - - - - - - - - - - - - - - - */
}