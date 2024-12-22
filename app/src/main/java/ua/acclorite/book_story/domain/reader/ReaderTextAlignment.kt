package ua.acclorite.book_story.domain.reader

enum class ReaderTextAlignment {
    START, JUSTIFY, CENTER, END
}

fun String.toTextAlignment(): ReaderTextAlignment {
    return ReaderTextAlignment.valueOf(this)
}