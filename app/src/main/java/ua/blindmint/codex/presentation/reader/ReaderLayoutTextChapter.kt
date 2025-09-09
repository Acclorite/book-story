/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.reader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.domain.reader.ReaderText.Chapter
import ua.blindmint.codex.domain.reader.ReaderTextAlignment
import ua.blindmint.codex.presentation.core.components.common.StyledText

@Composable
fun LazyItemScope.ReaderLayoutTextChapter(
    chapter: Chapter,
    chapterTitleAlignment: ReaderTextAlignment,
    fontColor: Color,
    sidePadding: Dp,
    highlightedReading: Boolean,
    highlightedReadingThickness: FontWeight
) {
    Column(
        Modifier
            .animateItem(
                fadeInSpec = null,
                fadeOutSpec = null
            )
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(22.dp))

        StyledText(
            text = buildAnnotatedString { append(chapter.title) },
            modifier = Modifier
                .padding(horizontal = sidePadding)
                .fillMaxWidth(),
            style = (if (!chapter.nested) MaterialTheme.typography.headlineMedium
            else MaterialTheme.typography.headlineSmall)
                .copy(
                    color = fontColor,
                    textAlign = chapterTitleAlignment.textAlignment
                ),
            highlightText = highlightedReading,
            highlightThickness = highlightedReadingThickness
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = fontColor.copy(0.4f))
        Spacer(modifier = Modifier.height(16.dp))
    }
}