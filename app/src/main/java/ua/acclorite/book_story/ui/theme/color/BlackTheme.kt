/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.common.helpers.LocalSettings

@Composable
fun blackTheme(initialTheme: ColorScheme): ColorScheme {
    val settings = LocalSettings.current

    val surfaceDarker = remember { 3f }
    val surfaceContainerDarker = remember(settings.absoluteDark.value) {
        if (settings.absoluteDark.lastValue) 3f else 1.95f
    }

    return initialTheme.copy(
        surface = initialTheme.surface.run {
            if (settings.absoluteDark.value) {
                return@run Color.Black
            }

            darkenBy(surfaceDarker)
        },
        surfaceContainer = initialTheme.surfaceContainer.darkenBy(
            surfaceContainerDarker
        ),
        surfaceContainerLowest = initialTheme.surfaceContainerLowest.darkenBy(
            surfaceContainerDarker
        ),
        surfaceContainerLow = initialTheme.surfaceContainerLow.darkenBy(
            surfaceContainerDarker
        ),
        surfaceContainerHigh = initialTheme.surfaceContainerHigh.darkenBy(
            surfaceContainerDarker
        ),
        surfaceContainerHighest = initialTheme.surfaceContainerHighest.darkenBy(
            surfaceContainerDarker
        )
    )
}

private fun Color.darkenBy(value: Float): Color {
    return copy(
        red = red / value,
        green = green / value,
        blue = blue / value
    )
}