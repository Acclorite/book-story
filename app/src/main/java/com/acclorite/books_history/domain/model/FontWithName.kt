package com.acclorite.books_history.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import com.acclorite.books_history.util.UIText

@Immutable
data class FontWithName(
    val id: String,
    val fontName: UIText,
    val font: FontFamily
)