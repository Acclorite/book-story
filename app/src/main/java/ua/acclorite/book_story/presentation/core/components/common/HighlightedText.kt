package ua.acclorite.book_story.presentation.core.components.common

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import kotlin.math.roundToInt

@Composable
fun HighlightedText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    highlightThickness: FontWeight,
    style: TextStyle
) {
    val highlightedText = remember(text, highlightThickness) {
        buildAnnotatedString {
            text.splitAnnotatedString().forEach { word ->
                if (word.text.none { it.isLetter() }) {
                    append(word)
                    append(" ")
                    return@forEach
                }

                val textWord = word.text.filterDigitsAtStartAndEnd()
                val digitsAtStart = word.text.digitsAtStart()
                val highlightArea = when (textWord.length) {
                    3 -> 1
                    else -> (textWord.length * 0.5f).roundToInt()
                } + digitsAtStart

                if (digitsAtStart > 0) {
                    append(word.subSequence(0, digitsAtStart))
                }

                withStyle(style = SpanStyle(fontWeight = highlightThickness)) {
                    append(word.subSequence(digitsAtStart, highlightArea))
                }

                append(word.subSequence(highlightArea, word.text.length))
                append(" ")
            }
        }
    }

    BasicText(
        text = highlightedText,
        modifier = modifier,
        style = style,
        overflow = TextOverflow.Ellipsis
    )
}

private fun String.digitsAtStart(): Int {
    var count = 0
    for (char in this) {
        if (!char.isLetter()) count++
        else break
    }
    return count
}

private fun String.filterDigitsAtStartAndEnd(): String {
    return dropWhile { !it.isLetter() }.dropLastWhile { !it.isLetter() }
}

private fun AnnotatedString.splitAnnotatedString(): List<AnnotatedString> {
    val result = mutableListOf<AnnotatedString>()

    val wordRegex = Regex("\\S+")
    wordRegex.findAll(this).forEach { matchResult ->
        val wordStart = matchResult.range.first
        val wordEnd = matchResult.range.last + 1

        val word = subSequence(wordStart, wordEnd)
        result.add(word)
    }

    return result
}