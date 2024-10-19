package ua.acclorite.book_story.presentation.core.util

fun String.removeTrailingZero(): String {
    if (!this.contains('.'))
        return this
    return this
        .dropLastWhile { it == '0' }
        .dropLastWhile { it == '.' }
}

fun Double.removeDigits(digits: Int) = "%.${digits}f".format(this).replace(",", ".")

fun Float.calculateProgress(digits: Int): String {
    return (this * 100)
        .toDouble()
        .removeDigits(digits)
        .removeTrailingZero()
        .dropWhile { it == '-' }
}

fun Float.coerceAndPreventNaN(): Float {
    if (isNaN()) return 0f
    return this.coerceIn(0f, 1f)
}

fun String.clearMarkdown(): String {
    return replace(Regex("_|\\*\\*"), "")
}