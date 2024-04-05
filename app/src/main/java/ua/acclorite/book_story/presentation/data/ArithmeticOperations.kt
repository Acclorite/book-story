package ua.acclorite.book_story.presentation.data

fun String.removeTrailingZero(): String {
    if (!this.contains('.'))
        return this
    return this
        .dropLastWhile { it == '0' }
        .dropLastWhile { it == '.' }
}

fun Double.removeDigits(digits: Int) = "%.${digits}f".format(this).replace(",", ".")

