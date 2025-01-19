package ua.acclorite.book_story.domain.reader

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontWeight

@Immutable
enum class ReaderFontThickness(val thickness: FontWeight) {
    THIN(FontWeight.Thin),
    EXTRA_LIGHT(FontWeight.ExtraLight),
    LIGHT(FontWeight.Light),
    NORMAL(FontWeight.Normal),
    MEDIUM(FontWeight.Medium)
}

fun String.toFontThickness(): ReaderFontThickness {
    return ReaderFontThickness.valueOf(this)
}