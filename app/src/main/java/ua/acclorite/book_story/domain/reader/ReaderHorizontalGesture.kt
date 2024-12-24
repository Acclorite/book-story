package ua.acclorite.book_story.domain.reader

enum class ReaderHorizontalGesture {
    OFF,
    ON,
    INVERSE
}

fun String.toHorizontalGesture(): ReaderHorizontalGesture {
    return ReaderHorizontalGesture.valueOf(this)
}