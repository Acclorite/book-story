package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val primaryLight = Color(0xFF62514F)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFDBC4C1)
private val onPrimaryContainerLight = Color(0xFF231615)
private val secondaryLight = Color(0xFF6B5A58)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFF5DDDA)
private val onSecondaryContainerLight = Color(0xFF251917)
private val tertiaryLight = Color(0xFF775653)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFFFDAD6)
private val onTertiaryContainerLight = Color(0xFF2C1513)
private val errorLight = Color(0xFFB3261E)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFF9DEDC)
private val onErrorContainerLight = Color(0xFF410E0B)
private val backgroundLight = Color(0xFFFEF8F7)
private val onBackgroundLight = Color(0xFF1E1B1A)
private val surfaceLight = Color(0xFFFEF8F7)
private val onSurfaceLight = Color(0xFF1E1B1A)
private val surfaceVariantLight = Color(0xFFE9E1DF)
private val onSurfaceVariantLight = Color(0xFF4A4645)
private val outlineLight = Color(0xFF7A7574)
private val outlineVariantLight = Color(0xFFCCC5C4)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF332F2F)
private val inverseOnSurfaceLight = Color(0xFFF7EFEE)
private val inversePrimaryLight = Color(0xFFC2ACA9)
private val surfaceDimLight = Color(0xFFDED9D7)
private val surfaceBrightLight = Color(0xFFFEF8F7)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF8F2F1)
private val surfaceContainerLight = Color(0xFFF2ECEB)
private val surfaceContainerHighLight = Color(0xFFEDE7E6)
private val surfaceContainerHighestLight = Color(0xFFE9E1DF)


private val primaryDark = Color(0xFFC2ACA9)
private val onPrimaryDark = Color(0xFF372927)
private val primaryContainerDark = Color(0xFF4C3C3A)
private val onPrimaryContainerDark = Color(0xFFDBC4C1)
private val secondaryDark = Color(0xFFD8C2BF)
private val onSecondaryDark = Color(0xFF3B2D2B)
private val secondaryContainerDark = Color(0xFF534341)
private val onSecondaryContainerDark = Color(0xFFF5DDDA)
private val tertiaryDark = Color(0xFFE7BDB8)
private val onTertiaryDark = Color(0xFF442926)
private val tertiaryContainerDark = Color(0xFF5D3F3C)
private val onTertiaryContainerDark = Color(0xFFFFDAD6)
private val errorDark = Color(0xFFF2B8B5)
private val onErrorDark = Color(0xFF601410)
private val errorContainerDark = Color(0xFF8C1D18)
private val onErrorContainerDark = Color(0xFFF9DEDC)
private val backgroundDark = Color(0xFF171211)
private val onBackgroundDark = Color(0xFFE9E1DF)
private val surfaceDark = Color(0xFF141110)
private val onSurfaceDark = Color(0xFFE9E1DF)
private val surfaceVariantDark = Color(0xFF4A4645)
private val onSurfaceVariantDark = Color(0xFFCCC5C4)
private val outlineDark = Color(0xFF968F8E)
private val outlineVariantDark = Color(0xFF4A4645)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFE9E1DF)
private val inverseOnSurfaceDark = Color(0xFF332F2F)
private val inversePrimaryDark = Color(0xFF62514F)
private val surfaceDimDark = Color(0xFF171211)
private val surfaceBrightDark = Color(0xFF3D3837)
private val surfaceContainerLowestDark = Color(0xFF0D0B0A)
private val surfaceContainerLowDark = Color(0xFF1E1B1A)
private val surfaceContainerDark = Color(0xFF231F1F)
private val surfaceContainerHighDark = Color(0xFF2D2929)
private val surfaceContainerHighestDark = Color(0xFF383433)


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