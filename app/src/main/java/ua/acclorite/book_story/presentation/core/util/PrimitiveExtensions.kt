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

fun Float?.coerceAndPreventNaN(): Float {
    if (this == null) return 0f
    if (isNaN()) return 0f
    return this.coerceIn(0f, 1f)
}

fun String.clearMarkdown(): String {
    return replace(Regex("(_+)|(\\*+)"), "")
}

fun String.clearAllMarkdown(): String {
    return replace(Regex("(_+)|(\\*+)|(#+)"), "").trim()
}

fun String.containsVisibleText(): Boolean {
    return any { it.isVisibleCharacter() }
}

fun Char.isVisibleCharacter(): Boolean {
    return when (this.category) {
        CharCategory.UPPERCASE_LETTER,
        CharCategory.LOWERCASE_LETTER,
        CharCategory.TITLECASE_LETTER,
        CharCategory.MODIFIER_LETTER,
        CharCategory.OTHER_LETTER,
        CharCategory.DECIMAL_DIGIT_NUMBER,
        CharCategory.LETTER_NUMBER,
        CharCategory.OTHER_NUMBER,
        CharCategory.MATH_SYMBOL,
        CharCategory.CURRENCY_SYMBOL,
        CharCategory.OTHER_SYMBOL,
        CharCategory.INITIAL_QUOTE_PUNCTUATION,
        CharCategory.FINAL_QUOTE_PUNCTUATION,
        CharCategory.CONNECTOR_PUNCTUATION,
        CharCategory.DASH_PUNCTUATION,
        CharCategory.START_PUNCTUATION,
        CharCategory.END_PUNCTUATION,
        CharCategory.OTHER_PUNCTUATION -> true

        else -> false
    }
}