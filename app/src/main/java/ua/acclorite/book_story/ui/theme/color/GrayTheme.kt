/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val primaryLight = Color(0xFF000000)
private val onPrimaryLight = Color(0xFFE2E2E2)
private val primaryContainerLight = Color(0xFF3B3B3B)
private val onPrimaryContainerLight = Color(0xFFFFFFFF)
private val secondaryLight = Color(0xFF5E5E5E)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFD4D4D4)
private val onSecondaryContainerLight = Color(0xFF1B1B1B)
private val tertiaryLight = Color(0xFF3B3B3B)
private val onTertiaryLight = Color(0xFFE2E2E2)
private val tertiaryContainerLight = Color(0xFF747474)
private val onTertiaryContainerLight = Color(0xFFFFFFFF)
private val errorLight = Color(0xFFB3261E)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFF9DEDC)
private val onErrorContainerLight = Color(0xFF410E0B)
private val backgroundLight = Color(0xFFF9F9F9)
private val onBackgroundLight = Color(0xFF1B1B1B)
private val surfaceLight = Color(0xFFF9F9F9)
private val onSurfaceLight = Color(0xFF1B1B1B)
private val surfaceVariantLight = Color(0xFFE2E2E2)
private val onSurfaceVariantLight = Color(0xFF474747)
private val outlineLight = Color(0xFF777777)
private val outlineVariantLight = Color(0xFFC6C6C6)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF131313)
private val inverseOnSurfaceLight = Color(0xFFE2E2E2)
private val inversePrimaryLight = Color(0xFFFFFFFF)
private val surfaceDimLight = Color(0xFFDADADA)
private val surfaceBrightLight = Color(0xFFF9F9F9)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF3F3F3)
private val surfaceContainerLight = Color(0xFFEEEEEE)
private val surfaceContainerHighLight = Color(0xFFE8E8E8)
private val surfaceContainerHighestLight = Color(0xFFE2E2E2)


private val primaryDark = Color(0xFFFFFFFF)
private val onPrimaryDark = Color(0xFF1B1B1B)
private val primaryContainerDark = Color(0xFFD4D4D4)
private val onPrimaryContainerDark = Color(0xFF000000)
private val secondaryDark = Color(0xFFC6C6C6)
private val onSecondaryDark = Color(0xFF1B1B1B)
private val secondaryContainerDark = Color(0xFF474747)
private val onSecondaryContainerDark = Color(0xFFE2E2E2)
private val tertiaryDark = Color(0xFFE2E2E2)
private val onTertiaryDark = Color(0xFF1B1B1B)
private val tertiaryContainerDark = Color(0xFF919191)
private val onTertiaryContainerDark = Color(0xFF000000)
private val errorDark = Color(0xFFF2B8B5)
private val onErrorDark = Color(0xFF601410)
private val errorContainerDark = Color(0xFF8C1D18)
private val onErrorContainerDark = Color(0xFFF9DEDC)
private val backgroundDark = Color(0xFF131313)
private val onBackgroundDark = Color(0xFFE2E2E2)
private val surfaceDark = Color(0xFF101010)
private val onSurfaceDark = Color(0xFFE2E2E2)
private val surfaceVariantDark = Color(0xFF474747)
private val onSurfaceVariantDark = Color(0xFFC6C6C6)
private val outlineDark = Color(0xFF919191)
private val outlineVariantDark = Color(0xFF474747)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFF9F9F9)
private val inverseOnSurfaceDark = Color(0xFF1B1B1B)
private val inversePrimaryDark = Color(0xFF000000)
private val surfaceDimDark = Color(0xFF131313)
private val surfaceBrightDark = Color(0xFF393939)
private val surfaceContainerLowestDark = Color(0xFF0A0A0A)
private val surfaceContainerLowDark = Color(0xFF1B1B1B)
private val surfaceContainerDark = Color(0xFF1F1F1F)
private val surfaceContainerHighDark = Color(0xFF2A2A2A)
private val surfaceContainerHighestDark = Color(0xFF353535)


@Composable
fun grayTheme(isDark: Boolean): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = primaryDark,
            onPrimary = onPrimaryDark,
            primaryContainer = primaryContainerDark,
            onPrimaryContainer = onPrimaryContainerDark,
            secondary = secondaryDark,
            onSecondary = onSecondaryDark,
            secondaryContainer = secondaryContainerDark,
            onSecondaryContainer = onSecondaryContainerDark,
            tertiary = tertiaryDark,
            onTertiary = onTertiaryDark,
            tertiaryContainer = tertiaryContainerDark,
            onTertiaryContainer = onTertiaryContainerDark,
            error = errorDark,
            onError = onErrorDark,
            errorContainer = errorContainerDark,
            onErrorContainer = onErrorContainerDark,
            background = backgroundDark,
            onBackground = onBackgroundDark,
            surface = surfaceDark,
            onSurface = onSurfaceDark,
            surfaceVariant = surfaceVariantDark,
            onSurfaceVariant = onSurfaceVariantDark,
            outline = outlineDark,
            outlineVariant = outlineVariantDark,
            scrim = scrimDark,
            inverseSurface = inverseSurfaceDark,
            inverseOnSurface = inverseOnSurfaceDark,
            inversePrimary = inversePrimaryDark,
            surfaceDim = surfaceDimDark,
            surfaceBright = surfaceBrightDark,
            surfaceContainerLowest = surfaceContainerLowestDark,
            surfaceContainerLow = surfaceContainerLowDark,
            surfaceContainer = surfaceContainerDark,
            surfaceContainerHigh = surfaceContainerHighDark,
            surfaceContainerHighest = surfaceContainerHighestDark,
        )
    } else {
        lightColorScheme(
            primary = primaryLight,
            onPrimary = onPrimaryLight,
            primaryContainer = primaryContainerLight,
            onPrimaryContainer = onPrimaryContainerLight,
            secondary = secondaryLight,
            onSecondary = onSecondaryLight,
            secondaryContainer = secondaryContainerLight,
            onSecondaryContainer = onSecondaryContainerLight,
            tertiary = tertiaryLight,
            onTertiary = onTertiaryLight,
            tertiaryContainer = tertiaryContainerLight,
            onTertiaryContainer = onTertiaryContainerLight,
            error = errorLight,
            onError = onErrorLight,
            errorContainer = errorContainerLight,
            onErrorContainer = onErrorContainerLight,
            background = backgroundLight,
            onBackground = onBackgroundLight,
            surface = surfaceLight,
            onSurface = onSurfaceLight,
            surfaceVariant = surfaceVariantLight,
            onSurfaceVariant = onSurfaceVariantLight,
            outline = outlineLight,
            outlineVariant = outlineVariantLight,
            scrim = scrimLight,
            inverseSurface = inverseSurfaceLight,
            inverseOnSurface = inverseOnSurfaceLight,
            inversePrimary = inversePrimaryLight,
            surfaceDim = surfaceDimLight,
            surfaceBright = surfaceBrightLight,
            surfaceContainerLowest = surfaceContainerLowestLight,
            surfaceContainerLow = surfaceContainerLowLight,
            surfaceContainer = surfaceContainerLight,
            surfaceContainerHigh = surfaceContainerHighLight,
            surfaceContainerHighest = surfaceContainerHighestLight,
        )
    }
}