package ua.acclorite.book_story.domain.reader

import androidx.compose.runtime.Immutable

@Immutable
enum class ReaderProgressCount {
    PERCENTAGE,
    QUANTITY
}

fun String.toProgressCount(): ReaderProgressCount {
    return ReaderProgressCount.valueOf(this)
}