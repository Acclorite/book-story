package com.acclorite.books_history.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily

@Immutable
data class FontWithName(
    val fontName: String,
    val font: FontFamily
)