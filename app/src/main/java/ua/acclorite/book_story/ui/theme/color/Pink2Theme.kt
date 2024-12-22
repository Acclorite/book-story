package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val primaryLight = Color(0xFF3E5C38)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFB0D3A5)
private val onPrimaryContainerLight = Color(0xFF031F03)
private val secondaryLight = Color(0xFF7C5264)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFFFD8E6)
private val onSecondaryContainerLight = Color(0xFF301120)
private val tertiaryLight = Color(0xFF535B8B)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFDEE0FF)
private val onTertiaryContainerLight = Color(0xFF0E1744)
private val errorLight = Color(0xFFB3261E)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFF9DEDC)
private val onErrorContainerLight = Color(0xFF410E0B)
private val backgroundLight = Color(0xFFF7F8FF)
private val onBackgroundLight = Color(0xFF191B27)
private val surfaceLight = Color(0xFFF7F8FF)
private val onSurfaceLight = Color(0xFF191B27)
private val surfaceVariantLight = Color(0xFFE1E1F3)
private val onSurfaceVariantLight = Color(0xFF444654)
private val outlineLight = Color(0xFF747584)
private val outlineVariantLight = Color(0xFFC5C5D6)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF2E303D)
private val inverseOnSurfaceLight = Color(0xFFF0EFFF)
private val inversePrimaryLight = Color(0xFF98BA8D)
private val surfaceDimLight = Color(0xFFD8D9EA)
private val surfaceBrightLight = Color(0xFFF7F8FF)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF2F3FF)
private val surfaceContainerLight = Color(0xFFECEDFF)
private val surfaceContainerHighLight = Color(0xFFE6E7F9)
private val surfaceContainerHighestLight = Color(0xFFE1E1F3)


private val primaryDark = Color(0xFF98BA8D)
private val onPrimaryDark = Color(0xFF163212)
private val primaryContainerDark = Color(0xFF2A4724)
private val onPrimaryContainerDark = Color(0xFFB0D3A5)
private val secondaryDark = Color(0xFFEDB8CD)
private val onSecondaryDark = Color(0xFF482535)
private val secondaryContainerDark = Color(0xFF623B4C)
private val onSecondaryContainerDark = Color(0xFFFFD8E6)
private val tertiaryDark = Color(0xFFBCC3FA)
private val onTertiaryDark = Color(0xFF252D5A)
private val tertiaryContainerDark = Color(0xFF3C4472)
private val onTertiaryContainerDark = Color(0xFFDEE0FF)
private val errorDark = Color(0xFFF2B8B5)
private val onErrorDark = Color(0xFF601410)
private val errorContainerDark = Color(0xFF8C1D18)
private val onErrorContainerDark = Color(0xFFF9DEDC)
private val backgroundDark = Color(0xFF12131F)
private val onBackgroundDark = Color(0xFFE1E1F3)
private val surfaceDark = Color(0xFF12131B)
private val onSurfaceDark = Color(0xFFE1E1F3)
private val surfaceVariantDark = Color(0xFF444654)
private val onSurfaceVariantDark = Color(0xFFC5C5D6)
private val outlineDark = Color(0xFF8F8F9F)
private val outlineVariantDark = Color(0xFF444654)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFE1E1F3)
private val inverseOnSurfaceDark = Color(0xFF2E303D)
private val inversePrimaryDark = Color(0xFF3E5C38)
private val surfaceDimDark = Color(0xFF12131F)
private val surfaceBrightDark = Color(0xFF373846)
private val surfaceContainerLowestDark = Color(0xFF0E0E13)
private val surfaceContainerLowDark = Color(0xFF1A1C27)
private val surfaceContainerDark = Color(0xFF1F202B)
private val surfaceContainerHighDark = Color(0xFF292A36)
private val surfaceContainerHighestDark = Color(0xFF333442)


@Composable
fun pink2Theme(isDark: Boolean): ColorScheme {
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