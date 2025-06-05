/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings

object ScrollbarData {
    val primarySelectableScrollbar
        @Composable get() = ScrollbarSettings(
            thumbUnselectedColor = MaterialTheme.colorScheme.secondary,
            thumbSelectedColor = MaterialTheme.colorScheme.secondary.copy(0.8f),
            hideDelayMillis = 2000,
            durationAnimationMillis = 300,
            selectionMode = ScrollbarSelectionMode.Thumb,
            thumbThickness = 8.dp,
            scrollbarPadding = 4.dp
        )

    val primaryScrollbar
        @Composable get() = ScrollbarSettings(
            thumbUnselectedColor = MaterialTheme.colorScheme.secondary,
            thumbSelectedColor = MaterialTheme.colorScheme.secondary.copy(0.8f),
            hideDelayMillis = 2000,
            durationAnimationMillis = 300,
            selectionMode = ScrollbarSelectionMode.Disabled,
            thumbThickness = 8.dp,
            scrollbarPadding = 4.dp
        )

    val secondaryScrollbar
        @Composable get() = ScrollbarSettings(
            thumbUnselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
            hideDelayMillis = 500,
            durationAnimationMillis = 200,
            thumbMinLength = 0.4f,
            selectionMode = ScrollbarSelectionMode.Disabled,
            thumbThickness = 4.dp,
            thumbShape = RectangleShape,
            hideDisplacement = 0.dp,
            scrollbarPadding = 0.dp
        )
}