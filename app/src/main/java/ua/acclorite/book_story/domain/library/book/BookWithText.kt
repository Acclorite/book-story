package ua.acclorite.book_story.domain.library.book

import androidx.compose.runtime.Immutable

@Immutable
data class BookWithText(
    val book: Book,
    val text: List<String>
)