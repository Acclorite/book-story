package com.acclorite.books_history.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
data class ChipItem(
    val id: String,
    val title: String,
    val textStyle: TextStyle,
    val selected: Boolean
)
