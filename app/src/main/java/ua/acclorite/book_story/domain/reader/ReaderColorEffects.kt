package ua.acclorite.book_story.domain.reader

enum class ReaderColorEffects {
    OFF,
    GRAYSCALE,
    FONT,
    BACKGROUND
}

fun String.toColorEffects(): ReaderColorEffects {
    return ReaderColorEffects.valueOf(this)
}