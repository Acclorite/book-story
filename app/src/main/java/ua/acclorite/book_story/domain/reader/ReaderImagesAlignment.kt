package ua.acclorite.book_story.domain.reader

import androidx.compose.ui.Alignment

enum class ReaderImagesAlignment(val alignment: Alignment) {
    START(Alignment.CenterStart),
    CENTER(Alignment.Center),
    END(Alignment.CenterEnd)
}

fun String.toImagesAlignment(): ReaderImagesAlignment {
    return ReaderImagesAlignment.valueOf(this)
}