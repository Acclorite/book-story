package ua.acclorite.book_story.domain.reader

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import ua.acclorite.book_story.domain.ui.UIText

@Immutable
data class FontWithName(
    val id: String,
    val fontName: UIText,
    val font: FontFamily
)