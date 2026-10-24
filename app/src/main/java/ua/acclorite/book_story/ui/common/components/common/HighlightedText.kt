/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.common

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
    style: TextStyle,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    val highlightedText = remember(text, highlightThickness) {
        getHighlightedText(
            true,
            text,
            highlightThickness
        )
    }

    BasicText(
        text = highlightedText,
        modifier = modifier,
        style = style,
        maxLines = maxLines,
        minLines = minLines,
        overflow = overflow
    )
}

fun getHighlightedText(
    highlight: Boolean,
    text: AnnotatedString,
    highlightThickness: FontWeight,
    fontWeightIncrease: Int = 0
): AnnotatedString {
    return if (highlight) buildAnnotatedString {
        var previousEnd = 0
        Regex("\\S+").findAll(text.text).forEach { match ->
            val wordStart = match.range.first
            val wordEnd = match.range.last + 1
            append(text.subSequence(previousEnd, wordStart))
            previousEnd = wordEnd
            val word = text.subSequence(wordStart, wordEnd)
            if (word.text.none { it.isLetter() }) {
                append(word)
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

            val highlightedPart = word.subSequence(digitsAtStart, highlightArea)
            if (fontWeightIncrease > 0) {
                val highlightStart = length
                append(highlightedPart)

                val weightRanges = highlightedPart.spanStyles.filter { it.item.fontWeight != null }
                val boundaries = (listOf(0, highlightedPart.length) +
                        weightRanges.flatMap { listOf(it.start, it.end) }).distinct().sorted()

                boundaries.zipWithNext { start, end ->
                    val baseWeight = weightRanges.lastOrNull {
                        it.start <= start && it.end >= end
                    }?.item?.fontWeight ?: highlightThickness

                    addStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight(
                                (baseWeight.weight + fontWeightIncrease).coerceAtMost(900)
                            )
                        ),
                        start = highlightStart + start,
                        end = highlightStart + end
                    )
                }
            } else {
                withStyle(style = SpanStyle(fontWeight = highlightThickness)) {
                    append(highlightedPart)
                }
            }

            append(word.subSequence(highlightArea, word.text.length))
        }
        append(text.subSequence(previousEnd, text.length))
    } else text
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
