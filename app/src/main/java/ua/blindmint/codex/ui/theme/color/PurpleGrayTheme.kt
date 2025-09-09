/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val primaryLight = Color(0xFF55545D)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFCBC8D3)
private val onPrimaryContainerLight = Color(0xFF191921)
private val secondaryLight = Color(0xFF5F5D67)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFE4E1EC)
private val onSecondaryContainerLight = Color(0xFF1B1B23)
private val tertiaryLight = Color(0xFF5E5C71)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFE3E0F9)
private val onTertiaryContainerLight = Color(0xFF1A1A2C)
private val errorLight = Color(0xFFB3261E)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFF9DEDC)
private val onErrorContainerLight = Color(0xFF410E0B)
private val backgroundLight = Color(0xFFFCF8F9)
private val onBackgroundLight = Color(0xFF1C1B1D)
private val surfaceLight = Color(0xFFFCF8F9)
private val onSurfaceLight = Color(0xFF1C1B1D)
private val surfaceVariantLight = Color(0xFFE5E1E3)
private val onSurfaceVariantLight = Color(0xFF484648)
private val outlineLight = Color(0xFF777577)
private val outlineVariantLight = Color(0xFFC9C5C7)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF313032)
private val inverseOnSurfaceLight = Color(0xFFF4F0F1)
private val inversePrimaryLight = Color(0xFFB2AFBA)
private val surfaceDimLight = Color(0xFFDCD9DA)
private val surfaceBrightLight = Color(0xFFFCF8F9)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF6F3F4)
private val surfaceContainerLight = Color(0xFFF0EDEE)
private val surfaceContainerHighLight = Color(0xFFEAE7E8)
private val surfaceContainerHighestLight = Color(0xFFE5E1E3)


private val primaryDark = Color(0xFFB2AFBA)
private val onPrimaryDark = Color(0xFF2C2B34)
private val primaryContainerDark = Color(0xFF403F48)
private val onPrimaryContainerDark = Color(0xFFCBC8D3)
private val secondaryDark = Color(0xFFC8C5D0)
private val onSecondaryDark = Color(0xFF302F38)
private val secondaryContainerDark = Color(0xFF47464F)
private val onSecondaryContainerDark = Color(0xFFE4E1EC)
private val tertiaryDark = Color(0xFFC7C4DD)
private val onTertiaryDark = Color(0xFF2F2E42)
private val tertiaryContainerDark = Color(0xFF464559)
private val onTertiaryContainerDark = Color(0xFFE3E0F9)
private val errorDark = Color(0xFFF2B8B5)
private val onErrorDark = Color(0xFF601410)
private val errorContainerDark = Color(0xFF8C1D18)
private val onErrorContainerDark = Color(0xFFF9DEDC)
private val backgroundDark = Color(0xFF131314)
private val onBackgroundDark = Color(0xFFE5E1E3)
private val surfaceDark = Color(0xFF111111)
private val onSurfaceDark = Color(0xFFE5E1E3)
private val surfaceVariantDark = Color(0xFF484648)
private val onSurfaceVariantDark = Color(0xFFC9C5C7)
private val outlineDark = Color(0xFF929092)
private val outlineVariantDark = Color(0xFF484648)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFE5E1E3)
private val inverseOnSurfaceDark = Color(0xFF313032)
private val inversePrimaryDark = Color(0xFF55545D)
private val surfaceDimDark = Color(0xFF131314)
private val surfaceBrightDark = Color(0xFF39393A)
private val surfaceContainerLowestDark = Color(0xFF0A0A0B)
private val surfaceContainerLowDark = Color(0xFF1C1B1D)
private val surfaceContainerDark = Color(0xFF201F20)
private val surfaceContainerHighDark = Color(0xFF2A2A2B)
private val surfaceContainerHighestDark = Color(0xFF353435)


@Composable
fun purpleGrayTheme(isDark: Boolean): ColorScheme {
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