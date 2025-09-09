/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.reader

import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.blindmint.codex.domain.reader.FontWithName
import ua.blindmint.codex.domain.reader.ReaderFontThickness
import ua.blindmint.codex.domain.reader.ReaderText
import ua.blindmint.codex.domain.reader.ReaderTextAlignment
import ua.blindmint.codex.domain.util.HorizontalAlignment
import ua.blindmint.codex.ui.reader.ReaderEvent

@Composable
fun LazyItemScope.ReaderLayoutText(
    activity: ComponentActivity,
    showMenu: Boolean,
    entry: ReaderText,
    imagesCornersRoundness: Dp,
    imagesAlignment: HorizontalAlignment,
    imagesWidth: Float,
    imagesColorEffects: ColorFilter?,
    fontFamily: FontWithName,
    fontColor: Color,
    lineHeight: TextUnit,
    fontThickness: ReaderFontThickness,
    fontStyle: FontStyle,
    chapterTitleAlignment: ReaderTextAlignment,
    textAlignment: ReaderTextAlignment,
    horizontalAlignment: Alignment.Horizontal,
    fontSize: TextUnit,
    letterSpacing: TextUnit,
    sidePadding: Dp,
    paragraphIndentation: TextUnit,
    fullscreenMode: Boolean,
    doubleClickTranslation: Boolean,
    highlightedReading: Boolean,
    highlightedReadingThickness: FontWeight,
    toolbarHidden: Boolean,
    openTranslator: (ReaderEvent.OnOpenTranslator) -> Unit,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit
) {
    when (entry) {
        is ReaderText.Image -> {
            ReaderLayoutTextImage(
                entry = entry,
                sidePadding = sidePadding,
                imagesCornersRoundness = imagesCornersRoundness,
                imagesAlignment = imagesAlignment,
                imagesWidth = imagesWidth,
                imagesColorEffects = imagesColorEffects
            )
        }

        is ReaderText.Separator -> {
            ReaderLayoutTextSeparator(
                sidePadding = sidePadding,
                fontColor = fontColor
            )
        }

        is ReaderText.Chapter -> {
            ReaderLayoutTextChapter(
                chapter = entry,
                chapterTitleAlignment = chapterTitleAlignment,
                fontColor = fontColor,
                sidePadding = sidePadding,
                highlightedReading = highlightedReading,
                highlightedReadingThickness = highlightedReadingThickness
            )
        }

        is ReaderText.Text -> {
            ReaderLayoutTextParagraph(
                paragraph = entry,
                activity = activity,
                showMenu = showMenu,
                fontFamily = fontFamily,
                fontColor = fontColor,
                lineHeight = lineHeight,
                fontThickness = fontThickness,
                fontStyle = fontStyle,
                textAlignment = textAlignment,
                horizontalAlignment = horizontalAlignment,
                fontSize = fontSize,
                letterSpacing = letterSpacing,
                sidePadding = sidePadding,
                paragraphIndentation = paragraphIndentation,
                fullscreenMode = fullscreenMode,
                doubleClickTranslation = doubleClickTranslation,
                highlightedReading = highlightedReading,
                highlightedReadingThickness = highlightedReadingThickness,
                toolbarHidden = toolbarHidden,
                openTranslator = openTranslator,
                menuVisibility = menuVisibility
            )
        }
    }
}