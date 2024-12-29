package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val primaryLight = Color(0xFF66530B)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFE2C776)
private val onPrimaryContainerLight = Color(0xFF211900)
private val secondaryLight = Color(0xFF685E40)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFF1E1BB)
private val onSecondaryContainerLight = Color(0xFF221B04)
private val tertiaryLight = Color(0xFF46664B)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFC7ECCA)
private val onTertiaryContainerLight = Color(0xFF02210C)
private val errorLight = Color(0xFFB3261E)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFF9DEDC)
private val onErrorContainerLight = Color(0xFF410E0B)
private val backgroundLight = Color(0xFFFBF9F8)
private val onBackgroundLight = Color(0xFF1B1B1B)
private val surfaceLight = Color(0xFFFBF9F8)
private val onSurfaceLight = Color(0xFF1B1B1B)
private val surfaceVariantLight = Color(0xFFE2E2E2)
private val onSurfaceVariantLight = Color(0xFF474747)
private val outlineLight = Color(0xFF767676)
private val outlineVariantLight = Color(0xFFC6C6C6)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF303030)
private val inverseOnSurfaceLight = Color(0xFFF1F1F1)
private val inversePrimaryLight = Color(0xFFC8AF60)
private val surfaceDimLight = Color(0xFFDBDAD9)
private val surfaceBrightLight = Color(0xFFFBF9F8)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF5F3F3)
private val surfaceContainerLight = Color(0xFFEFEDED)
private val surfaceContainerHighLight = Color(0xFFE9E8E7)
private val surfaceContainerHighestLight = Color(0xFFE2E2E2)

private val primaryDark = Color(0xFFC8AF60)
private val onPrimaryDark = Color(0xFF372B00)
private val primaryContainerDark = Color(0xFF4E3E00)
private val onPrimaryContainerDark = Color(0xFFE2C776)
private val secondaryDark = Color(0xFFD4C5A1)
private val onSecondaryDark = Color(0xFF383016)
private val secondaryContainerDark = Color(0xFF50462A)
private val onSecondaryContainerDark = Color(0xFFF1E1BB)
private val tertiaryDark = Color(0xFFACCFAF)
private val onTertiaryDark = Color(0xFF183720)
private val tertiaryContainerDark = Color(0xFF2F4E35)
private val onTertiaryContainerDark = Color(0xFFC7ECCA)
private val errorDark = Color(0xFFF2B8B5)
private val onErrorDark = Color(0xFF601410)
private val errorContainerDark = Color(0xFF8C1D18)
private val onErrorContainerDark = Color(0xFFF9DEDC)
private val backgroundDark = Color(0xFF131314)
private val onBackgroundDark = Color(0xFFE2E2E2)
private val surfaceDark = Color(0xFF111111)
private val onSurfaceDark = Color(0xFFE2E2E2)
private val surfaceVariantDark = Color(0xFF474747)
private val onSurfaceVariantDark = Color(0xFFC6C6C6)
private val outlineDark = Color(0xFF919191)
private val outlineVariantDark = Color(0xFF474747)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFE2E2E2)
private val inverseOnSurfaceDark = Color(0xFF303030)
private val inversePrimaryDark = Color(0xFF66530B)
private val surfaceDimDark = Color(0xFF131314)
private val surfaceBrightDark = Color(0xFF393939)
private val surfaceContainerLowestDark = Color(0xFF090A0A)
private val surfaceContainerLowDark = Color(0xFF1B1B1B)
private val surfaceContainerDark = Color(0xFF1F2020)
private val surfaceContainerHighDark = Color(0xFF292A2A)
private val surfaceContainerHighestDark = Color(0xFF343535)


@Composable
fun yellow2Theme(isDark: Boolean): ColorScheme {
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