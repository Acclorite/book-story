package ua.acclorite.book_story.domain.reader

import androidx.compose.runtime.Immutable

@Immutable
data class ChapterWithText(
    val chapter: Chapter,
    val text: List<String>
)