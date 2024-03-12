package ua.acclorite.book_story.domain.model

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.util.UIText

@Immutable
sealed class NullableBook(
    val book: Pair<Book, Boolean>?,
    val fileName: String?,
    val message: UIText?
) {
    class NotNull(book: Pair<Book, Boolean>) : NullableBook(book, null, null)
    class Null(
        fileName: String,
        message: UIText?
    ) : NullableBook(null, fileName, message)
}
