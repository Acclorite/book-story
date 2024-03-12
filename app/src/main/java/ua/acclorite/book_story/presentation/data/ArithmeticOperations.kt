package ua.acclorite.book_story.presentation.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.math.abs

fun String.removeTrailingZero(): String {
    if (!this.contains('.'))
        return this
    return this
        .dropLastWhile { it == '0' }
        .dropLastWhile { it == '.' }
}

fun Double.removeDigits(digits: Int) = "%.${digits}f".format(this).replace(",", ".")

fun calculateFamiliarity(string: String, target: String): Int {
    val targetCounts = target.lowercase().trim().groupingBy { it }.eachCount()
    val familiarity = string.lowercase().trim().sumOf { targetCounts.getOrDefault(it, 0) }
    return familiarity
}

fun Path.standardQuadFromTo(from: Offset, to: Offset) {
    quadraticTo(
        from.x,
        from.y,
        abs(from.x + to.x) / 2f,
        abs(from.y + to.y) / 2f
    )
}