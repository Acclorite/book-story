package ua.acclorite.book_story.domain.reader

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.AnnotatedString

@Immutable
sealed class ReaderText {
    @Immutable
    data class Chapter(val title: String) : ReaderText()

    @Immutable
    data class Text(val line: AnnotatedString) : ReaderText()

    @Immutable
    data object Separator : ReaderText()

    @Immutable
    data class Image(
        val imageBitmap: ImageBitmap
    ) : ReaderText()
}