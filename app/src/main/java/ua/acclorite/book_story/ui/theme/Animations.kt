/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.theme.model.Theme
import ua.acclorite.book_story.ui.theme.model.ThemeContrast

@Composable
private fun animateColor(targetColor: Color): Color {
    return animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(300),
        label = ""
    ).value
}

@Composable
fun animateColorScheme(targetColorScheme: ColorScheme): ColorScheme {
    return ColorScheme(
        primary = animateColor(targetColor = targetColorScheme.primary),
        onPrimary = animateColor(targetColor = targetColorScheme.onPrimary),
        primaryContainer = animateColor(targetColor = targetColorScheme.primaryContainer),
        onPrimaryContainer = animateColor(targetColor = targetColorScheme.onPrimaryContainer),
        secondary = animateColor(targetColor = targetColorScheme.secondary),
        onSecondary = animateColor(targetColor = targetColorScheme.onSecondary),
        secondaryContainer = animateColor(targetColor = targetColorScheme.secondaryContainer),
        onSecondaryContainer = animateColor(targetColor = targetColorScheme.onSecondaryContainer),
        tertiary = animateColor(targetColor = targetColorScheme.tertiary),
        onTertiary = animateColor(targetColor = targetColorScheme.onTertiary),
        tertiaryContainer = animateColor(targetColor = targetColorScheme.tertiaryContainer),
        onTertiaryContainer = animateColor(targetColor = targetColorScheme.onTertiaryContainer),
        error = animateColor(targetColor = targetColorScheme.error),
        errorContainer = animateColor(targetColor = targetColorScheme.errorContainer),
        onError = animateColor(targetColor = targetColorScheme.onError),
        onErrorContainer = animateColor(targetColor = targetColorScheme.onErrorContainer),
        background = animateColor(targetColor = targetColorScheme.background),
        onBackground = animateColor(targetColor = targetColorScheme.onBackground),
        surface = animateColor(targetColor = targetColorScheme.surface),
        onSurface = animateColor(targetColor = targetColorScheme.onSurface),
        surfaceVariant = animateColor(targetColor = targetColorScheme.surfaceVariant),
        onSurfaceVariant = animateColor(targetColor = targetColorScheme.onSurfaceVariant),
        outline = animateColor(targetColor = targetColorScheme.outline),
        inverseOnSurface = animateColor(targetColor = targetColorScheme.inverseOnSurface),
        inverseSurface = animateColor(targetColor = targetColorScheme.inverseSurface),
        inversePrimary = animateColor(targetColor = targetColorScheme.inversePrimary),
        surfaceTint = animateColor(targetColor = targetColorScheme.surfaceTint),
        outlineVariant = animateColor(targetColor = targetColorScheme.outlineVariant),
        scrim = animateColor(targetColor = targetColorScheme.scrim),
        surfaceBright = animateColor(targetColor = targetColorScheme.surfaceBright),
        surfaceDim = animateColor(targetColor = targetColorScheme.surfaceDim),
        surfaceContainer = animateColor(targetColor = targetColorScheme.surfaceContainer),
        surfaceContainerHigh = animateColor(targetColor = targetColorScheme.surfaceContainerHigh),
        surfaceContainerHighest = animateColor(targetColor = targetColorScheme.surfaceContainerHighest),
        surfaceContainerLow = animateColor(targetColor = targetColorScheme.surfaceContainerLow),
        surfaceContainerLowest = animateColor(targetColor = targetColorScheme.surfaceContainerLowest),
    )
}

@Composable
fun animatedColorScheme(
    theme: Theme,
    isDark: Boolean,
    isPureDark: Boolean,
    themeContrast: ThemeContrast
): ColorScheme {
    val colorScheme = colorScheme(
        theme = theme,
        darkTheme = isDark,
        isPureDark = isPureDark,
        themeContrast = themeContrast
    )

    return animateColorScheme(targetColorScheme = colorScheme)
}