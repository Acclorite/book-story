/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.reader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import ua.blindmint.codex.domain.util.HorizontalAlignment
import ua.blindmint.codex.presentation.core.components.common.StyledText

@Composable
fun ReaderProgressBar(
    progress: String,
    progressBarPadding: Dp,
    progressBarAlignment: HorizontalAlignment,
    progressBarFontSize: TextUnit,
    fontColor: Color,
    sidePadding: Dp
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = sidePadding,
                vertical = progressBarPadding
            ),
        contentAlignment = progressBarAlignment.alignment
    ) {
        DisableSelection {
            StyledText(
                text = progress,
                style = LocalTextStyle.current.copy(
                    color = fontColor,
                    fontSize = progressBarFontSize
                ),
                maxLines = 1
            )
        }
    }
}