package ua.acclorite.book_story.domain.model

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.util.CoverImage
import ua.acclorite.book_story.domain.util.UIText

@Immutable
sealed class NullableBook(
    val book: Book?,
    val coverImage: CoverImage? = null,
    val text: List<String> = emptyList(),
    val fileName: String?,
    val message: UIText?
) {
    class NotNull(
        book: Book,
        coverImage: CoverImage?,
        text: List<String>
    ) : NullableBook(
        book = book,
        text = text,
        coverImage = coverImage,
        fileName = null,
        message = null
    )

    class Null(
        fileName: String,
        message: UIText?
    ) : NullableBook(null, text = emptyList(), fileName = fileName, message = message)
}
