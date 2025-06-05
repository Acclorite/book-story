/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.common

import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun StyledText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    highlightText: Boolean = false,
    highlightThickness: FontWeight = FontWeight.SemiBold
) {
    when (highlightText) {
        true -> {
            HighlightedText(
                modifier = modifier,
                text = text,
                highlightThickness = highlightThickness,
                style = style.copy(
                    textDirection = TextDirection.Content,
                    color = style.color.takeOrElse { LocalContentColor.current }
                ),
                maxLines = maxLines,
                minLines = minLines,
                overflow = overflow
            )
        }

        false -> {
            BasicText(
                modifier = modifier,
                text = text,
                style = style.copy(
                    textDirection = TextDirection.Content,
                    color = style.color.takeOrElse { LocalContentColor.current }
                ),
                maxLines = maxLines,
                minLines = minLines,
                overflow = overflow
            )
        }
    }
}

@Composable
fun StyledText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    BasicText(
        modifier = modifier,
        text = text,
        style = style.copy(
            textDirection = TextDirection.Content,
            color = style.color.takeOrElse { LocalContentColor.current }
        ),
        maxLines = maxLines,
        minLines = minLines,
        overflow = overflow
    )
}