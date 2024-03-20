package ua.acclorite.book_story.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import ua.acclorite.book_story.domain.util.UIText

@Immutable
data class FontWithName(
    val id: String,
    val fontName: UIText,
    val font: FontFamily
)