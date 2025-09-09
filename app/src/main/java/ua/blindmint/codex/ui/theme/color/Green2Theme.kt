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

private val primaryLight = Color(0xFF475B35)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFBAD1A2)
private val onPrimaryContainerLight = Color(0xFF0D1E02)
private val secondaryLight = Color(0xFF56624A)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFDAE7C9)
private val onSecondaryContainerLight = Color(0xFF141E0C)
private val tertiaryLight = Color(0xFF386664)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFBBECE9)
private val onTertiaryContainerLight = Color(0xFF00201F)
private val errorLight = Color(0xFFB3261E)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFF9DEDC)
private val onErrorContainerLight = Color(0xFF410E0B)
private val backgroundLight = Color(0xFFF7FBED)
private val onBackgroundLight = Color(0xFF191D14)
private val surfaceLight = Color(0xFFF7FBED)
private val onSurfaceLight = Color(0xFF191D14)
private val surfaceVariantLight = Color(0xFFE0E4D6)
private val onSurfaceVariantLight = Color(0xFF44483E)
private val outlineLight = Color(0xFF73786C)
private val outlineVariantLight = Color(0xFFC4C8BA)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF2D3228)
private val inverseOnSurfaceLight = Color(0xFFEFF2E4)
private val inversePrimaryLight = Color(0xFFA1B88B)
private val surfaceDimLight = Color(0xFFD7DCCE)
private val surfaceBrightLight = Color(0xFFF7FBED)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF1F5E7)
private val surfaceContainerLight = Color(0xFFEBF0E1)
private val surfaceContainerHighLight = Color(0xFFE5EADC)
private val surfaceContainerHighestLight = Color(0xFFE0E4D6)


private val primaryDark = Color(0xFFA1B88B)
private val onPrimaryDark = Color(0xFF1F3110)
private val primaryContainerDark = Color(0xFF334522)
private val onPrimaryContainerDark = Color(0xFFBAD1A2)
private val secondaryDark = Color(0xFFBECBAE)
private val onSecondaryDark = Color(0xFF29341F)
private val secondaryContainerDark = Color(0xFF3F4A34)
private val onSecondaryContainerDark = Color(0xFFDAE7C9)
private val tertiaryDark = Color(0xFFA0CFCD)
private val onTertiaryDark = Color(0xFF003736)
private val tertiaryContainerDark = Color(0xFF1E4E4D)
private val onTertiaryContainerDark = Color(0xFFBBECE9)
private val errorDark = Color(0xFFF2B8B5)
private val onErrorDark = Color(0xFF601410)
private val errorContainerDark = Color(0xFF8C1D18)
private val onErrorContainerDark = Color(0xFFF9DEDC)
private val backgroundDark = Color(0xFF10150D)
private val onBackgroundDark = Color(0xFFE0E4D6)
private val surfaceDark = Color(0xFF0F120D)
private val onSurfaceDark = Color(0xFFE0E4D6)
private val surfaceVariantDark = Color(0xFF44483E)
private val onSurfaceVariantDark = Color(0xFFC4C8BA)
private val outlineDark = Color(0xFF8E9286)
private val outlineVariantDark = Color(0xFF44483E)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFE0E4D6)
private val inverseOnSurfaceDark = Color(0xFF2D3228)
private val inversePrimaryDark = Color(0xFF475B35)
private val surfaceDimDark = Color(0xFF10150D)
private val surfaceBrightDark = Color(0xFF363B31)
private val surfaceContainerLowestDark = Color(0xFF090B08)
private val surfaceContainerLowDark = Color(0xFF191D15)
private val surfaceContainerDark = Color(0xFF1D2119)
private val surfaceContainerHighDark = Color(0xFF272B23)
private val surfaceContainerHighestDark = Color(0xFF31362D)


@Composable
fun green2Theme(isDark: Boolean): ColorScheme {
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