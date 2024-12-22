package ua.acclorite.book_story.domain.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
data class ButtonItem(
    val id: String,
    val title: String,
    val textStyle: TextStyle,
    val selected: Boolean
)
