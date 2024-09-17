package ua.acclorite.book_story.domain.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.UIText

@Immutable
data class Book(
    val id: Int = 0,

    val title: String,
    val author: UIText,
    val description: String?,

    val textPath: String,
    val filePath: String,
    val coverImage: Uri?,

    val scrollIndex: Int,
    val scrollOffset: Int,
    val progress: Float,
    val chapters: List<Chapter> = emptyList(),

    val lastOpened: Long?,
    val category: Category,
)

@Immutable
data class BookWithText(
    val book: Book,
    val text: List<String>
)

@Immutable
data class BookWithCover(
    val book: Book,
    val coverImage: CoverImage?
)

@Immutable
data class BookWithTextAndCover(
    val book: Book,
    val coverImage: CoverImage?,
    val text: List<String>
)

@Immutable
sealed class NullableBook(
    val bookWithTextAndCover: BookWithTextAndCover?,
    val fileName: String?,
    val message: UIText?
) {
    class NotNull(
        bookWithTextAndCover: BookWithTextAndCover
    ) : NullableBook(
        bookWithTextAndCover = bookWithTextAndCover,
        fileName = null,
        message = null
    )

    class Null(
        fileName: String,
        message: UIText?
    ) : NullableBook(
        bookWithTextAndCover = null,
        fileName = fileName,
        message = message
    )
}