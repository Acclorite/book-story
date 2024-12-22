package ua.acclorite.book_story.domain.library.book

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.util.Selected

@Immutable
data class SelectableBook(
    val data: Book,
    val selected: Selected
)