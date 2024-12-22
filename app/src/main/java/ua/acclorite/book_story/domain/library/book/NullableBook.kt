package ua.acclorite.book_story.domain.library.book

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.ui.UIText

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