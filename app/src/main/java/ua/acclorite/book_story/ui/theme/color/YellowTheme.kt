package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.theme.ThemeContrast

private val primaryLight = Color(0xFF845415)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFFFDDBB)
private val onPrimaryContainerLight = Color(0xFF2B1700)
private val secondaryLight = Color(0xFF725A41)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFFDDDBD)
private val onSecondaryContainerLight = Color(0xFF281805)
private val tertiaryLight = Color(0xFF56633B)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFD9E9B6)
private val onTertiaryContainerLight = Color(0xFF141F01)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFFFF8F4)
private val onBackgroundLight = Color(0xFF211A14)
private val surfaceLight = Color(0xFFFFF8F4)
private val onSurfaceLight = Color(0xFF211A14)
private val surfaceVariantLight = Color(0xFFF1DFD0)
private val onSurfaceVariantLight = Color(0xFF50453A)
private val outlineLight = Color(0xFF827568)
private val outlineVariantLight = Color(0xFFD4C4B5)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF372F27)
private val inverseOnSurfaceLight = Color(0xFFFDEEE3)
private val inversePrimaryLight = Color(0xFFFABA73)
private val surfaceDimLight = Color(0xFFE5D8CC)
private val surfaceBrightLight = Color(0xFFFFF8F4)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFFFF1E6)
private val surfaceContainerLight = Color(0xFFFAEBE0)
private val surfaceContainerHighLight = Color(0xFFF4E6DA)
private val surfaceContainerHighestLight = Color(0xFFEEE0D5)

private val primaryLightMediumContrast = Color(0xFF623A00)
private val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
private val primaryContainerLightMediumContrast = Color(0xFF9D6A2A)
private val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryLightMediumContrast = Color(0xFF543F28)
private val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightMediumContrast = Color(0xFF897056)
private val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryLightMediumContrast = Color(0xFF3B4722)
private val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightMediumContrast = Color(0xFF6C7A50)
private val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val errorLightMediumContrast = Color(0xFF8C0009)
private val onErrorLightMediumContrast = Color(0xFFFFFFFF)
private val errorContainerLightMediumContrast = Color(0xFFDA342E)
private val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
private val backgroundLightMediumContrast = Color(0xFFFFF8F4)
private val onBackgroundLightMediumContrast = Color(0xFF211A14)
private val surfaceLightMediumContrast = Color(0xFFFFF8F4)
private val onSurfaceLightMediumContrast = Color(0xFF211A14)
private val surfaceVariantLightMediumContrast = Color(0xFFF1DFD0)
private val onSurfaceVariantLightMediumContrast = Color(0xFF4C4136)
private val outlineLightMediumContrast = Color(0xFF695D51)
private val outlineVariantLightMediumContrast = Color(0xFF86786C)
private val scrimLightMediumContrast = Color(0xFF000000)
private val inverseSurfaceLightMediumContrast = Color(0xFF372F27)
private val inverseOnSurfaceLightMediumContrast = Color(0xFFFDEEE3)
private val inversePrimaryLightMediumContrast = Color(0xFFFABA73)
private val surfaceDimLightMediumContrast = Color(0xFFE5D8CC)
private val surfaceBrightLightMediumContrast = Color(0xFFFFF8F4)
private val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightMediumContrast = Color(0xFFFFF1E6)
private val surfaceContainerLightMediumContrast = Color(0xFFFAEBE0)
private val surfaceContainerHighLightMediumContrast = Color(0xFFF4E6DA)
private val surfaceContainerHighestLightMediumContrast = Color(0xFFEEE0D5)

private val primaryLightHighContrast = Color(0xFF351D00)
private val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
private val primaryContainerLightHighContrast = Color(0xFF623A00)
private val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val secondaryLightHighContrast = Color(0xFF301F0A)
private val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightHighContrast = Color(0xFF543F28)
private val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryLightHighContrast = Color(0xFF1B2605)
private val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightHighContrast = Color(0xFF3B4722)
private val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val errorLightHighContrast = Color(0xFF4E0002)
private val onErrorLightHighContrast = Color(0xFFFFFFFF)
private val errorContainerLightHighContrast = Color(0xFF8C0009)
private val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
private val backgroundLightHighContrast = Color(0xFFFFF8F4)
private val onBackgroundLightHighContrast = Color(0xFF211A14)
private val surfaceLightHighContrast = Color(0xFFFFF8F4)
private val onSurfaceLightHighContrast = Color(0xFF000000)
private val surfaceVariantLightHighContrast = Color(0xFFF1DFD0)
private val onSurfaceVariantLightHighContrast = Color(0xFF2C2219)
private val outlineLightHighContrast = Color(0xFF4C4136)
private val outlineVariantLightHighContrast = Color(0xFF4C4136)
private val scrimLightHighContrast = Color(0xFF000000)
private val inverseSurfaceLightHighContrast = Color(0xFF372F27)
private val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
private val inversePrimaryLightHighContrast = Color(0xFFFFE8D4)
private val surfaceDimLightHighContrast = Color(0xFFE5D8CC)
private val surfaceBrightLightHighContrast = Color(0xFFFFF8F4)
private val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightHighContrast = Color(0xFFFFF1E6)
private val surfaceContainerLightHighContrast = Color(0xFFFAEBE0)
private val surfaceContainerHighLightHighContrast = Color(0xFFF4E6DA)
private val surfaceContainerHighestLightHighContrast = Color(0xFFEEE0D5)


private val primaryDark = Color(0xFFFABA73)
private val onPrimaryDark = Color(0xFF482900)
private val primaryContainerDark = Color(0xFF673D00)
private val onPrimaryContainerDark = Color(0xFFFFDDBB)
private val secondaryDark = Color(0xFFE0C1A3)
private val onSecondaryDark = Color(0xFF402D17)
private val secondaryContainerDark = Color(0xFF58432C)
private val onSecondaryContainerDark = Color(0xFFFDDDBD)
private val tertiaryDark = Color(0xFFBDCC9C)
private val onTertiaryDark = Color(0xFF293412)
private val tertiaryContainerDark = Color(0xFF3F4B26)
private val onTertiaryContainerDark = Color(0xFFD9E9B6)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF18120C)
private val onBackgroundDark = Color(0xFFEEE0D5)
private val surfaceDark = Color(0xFF18120C)
private val onSurfaceDark = Color(0xFFEEE0D5)
private val surfaceVariantDark = Color(0xFF50453A)
private val onSurfaceVariantDark = Color(0xFFD4C4B5)
private val outlineDark = Color(0xFF9D8E81)
private val outlineVariantDark = Color(0xFF50453A)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFEEE0D5)
private val inverseOnSurfaceDark = Color(0xFF372F27)
private val inversePrimaryDark = Color(0xFF845415)
private val surfaceDimDark = Color(0xFF18120C)
private val surfaceBrightDark = Color(0xFF403830)
private val surfaceContainerLowestDark = Color(0xFF130D07)
private val surfaceContainerLowDark = Color(0xFF211A14)
private val surfaceContainerDark = Color(0xFF251E17)
private val surfaceContainerHighDark = Color(0xFF302921)
private val surfaceContainerHighestDark = Color(0xFF3B332C)

private val primaryDarkMediumContrast = Color(0xFFFFBE76)
private val onPrimaryDarkMediumContrast = Color(0xFF241200)
private val primaryContainerDarkMediumContrast = Color(0xFFBE8543)
private val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
private val secondaryDarkMediumContrast = Color(0xFFE4C5A7)
private val onSecondaryDarkMediumContrast = Color(0xFF221302)
private val secondaryContainerDarkMediumContrast = Color(0xFFA78C70)
private val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
private val tertiaryDarkMediumContrast = Color(0xFFC2D1A0)
private val onTertiaryDarkMediumContrast = Color(0xFF0F1900)
private val tertiaryContainerDarkMediumContrast = Color(0xFF88966A)
private val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
private val errorDarkMediumContrast = Color(0xFFFFBAB1)
private val onErrorDarkMediumContrast = Color(0xFF370001)
private val errorContainerDarkMediumContrast = Color(0xFFFF5449)
private val onErrorContainerDarkMediumContrast = Color(0xFF000000)
private val backgroundDarkMediumContrast = Color(0xFF18120C)
private val onBackgroundDarkMediumContrast = Color(0xFFEEE0D5)
private val surfaceDarkMediumContrast = Color(0xFF18120C)
private val onSurfaceDarkMediumContrast = Color(0xFFFFFAF8)
private val surfaceVariantDarkMediumContrast = Color(0xFF50453A)
private val onSurfaceVariantDarkMediumContrast = Color(0xFFD9C8B9)
private val outlineDarkMediumContrast = Color(0xFFB0A092)
private val outlineVariantDarkMediumContrast = Color(0xFF8F8174)
private val scrimDarkMediumContrast = Color(0xFF000000)
private val inverseSurfaceDarkMediumContrast = Color(0xFFEEE0D5)
private val inverseOnSurfaceDarkMediumContrast = Color(0xFF302921)
private val inversePrimaryDarkMediumContrast = Color(0xFF693E00)
private val surfaceDimDarkMediumContrast = Color(0xFF18120C)
private val surfaceBrightDarkMediumContrast = Color(0xFF403830)
private val surfaceContainerLowestDarkMediumContrast = Color(0xFF130D07)
private val surfaceContainerLowDarkMediumContrast = Color(0xFF211A14)
private val surfaceContainerDarkMediumContrast = Color(0xFF251E17)
private val surfaceContainerHighDarkMediumContrast = Color(0xFF302921)
private val surfaceContainerHighestDarkMediumContrast = Color(0xFF3B332C)

private val primaryDarkHighContrast = Color(0xFFFFFAF8)
private val onPrimaryDarkHighContrast = Color(0xFF000000)
private val primaryContainerDarkHighContrast = Color(0xFFFFBE76)
private val onPrimaryContainerDarkHighContrast = Color(0xFF000000)
private val secondaryDarkHighContrast = Color(0xFFFFFAF8)
private val onSecondaryDarkHighContrast = Color(0xFF000000)
private val secondaryContainerDarkHighContrast = Color(0xFFE4C5A7)
private val onSecondaryContainerDarkHighContrast = Color(0xFF000000)
private val tertiaryDarkHighContrast = Color(0xFFF5FFDB)
private val onTertiaryDarkHighContrast = Color(0xFF000000)
private val tertiaryContainerDarkHighContrast = Color(0xFFC2D1A0)
private val onTertiaryContainerDarkHighContrast = Color(0xFF000000)
private val errorDarkHighContrast = Color(0xFFFFF9F9)
private val onErrorDarkHighContrast = Color(0xFF000000)
private val errorContainerDarkHighContrast = Color(0xFFFFBAB1)
private val onErrorContainerDarkHighContrast = Color(0xFF000000)
private val backgroundDarkHighContrast = Color(0xFF18120C)
private val onBackgroundDarkHighContrast = Color(0xFFEEE0D5)
private val surfaceDarkHighContrast = Color(0xFF18120C)
private val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
private val surfaceVariantDarkHighContrast = Color(0xFF50453A)
private val onSurfaceVariantDarkHighContrast = Color(0xFFFFFAF8)
private val outlineDarkHighContrast = Color(0xFFD9C8B9)
private val outlineVariantDarkHighContrast = Color(0xFFD9C8B9)
private val scrimDarkHighContrast = Color(0xFF000000)
private val inverseSurfaceDarkHighContrast = Color(0xFFEEE0D5)
private val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
private val inversePrimaryDarkHighContrast = Color(0xFF3F2400)
private val surfaceDimDarkHighContrast = Color(0xFF18120C)
private val surfaceBrightDarkHighContrast = Color(0xFF403830)
private val surfaceContainerLowestDarkHighContrast = Color(0xFF130D07)
private val surfaceContainerLowDarkHighContrast = Color(0xFF211A14)
private val surfaceContainerDarkHighContrast = Color(0xFF251E17)
private val surfaceContainerHighDarkHighContrast = Color(0xFF302921)
private val surfaceContainerHighestDarkHighContrast = Color(0xFF3B332C)


@Composable
fun yellowTheme(isDark: Boolean, themeContrast: ThemeContrast): ColorScheme {
    return if (isDark) {
        when (themeContrast) {
            ThemeContrast.STANDARD -> {
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
            }

            ThemeContrast.MEDIUM -> {
                darkColorScheme(
                    primary = primaryDarkMediumContrast,
                    onPrimary = onPrimaryDarkMediumContrast,
                    primaryContainer = primaryContainerDarkMediumContrast,
                    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
                    secondary = secondaryDarkMediumContrast,
                    onSecondary = onSecondaryDarkMediumContrast,
                    secondaryContainer = secondaryContainerDarkMediumContrast,
                    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
                    tertiary = tertiaryDarkMediumContrast,
                    onTertiary = onTertiaryDarkMediumContrast,
                    tertiaryContainer = tertiaryContainerDarkMediumContrast,
                    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
                    error = errorDarkMediumContrast,
                    onError = onErrorDarkMediumContrast,
                    errorContainer = errorContainerDarkMediumContrast,
                    onErrorContainer = onErrorContainerDarkMediumContrast,
                    background = backgroundDarkMediumContrast,
                    onBackground = onBackgroundDarkMediumContrast,
                    surface = surfaceDarkMediumContrast,
                    onSurface = onSurfaceDarkMediumContrast,
                    surfaceVariant = surfaceVariantDarkMediumContrast,
                    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
                    outline = outlineDarkMediumContrast,
                    outlineVariant = outlineVariantDarkMediumContrast,
                    scrim = scrimDarkMediumContrast,
                    inverseSurface = inverseSurfaceDarkMediumContrast,
                    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
                    inversePrimary = inversePrimaryDarkMediumContrast,
                    surfaceDim = surfaceDimDarkMediumContrast,
                    surfaceBright = surfaceBrightDarkMediumContrast,
                    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
                    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
                    surfaceContainer = surfaceContainerDarkMediumContrast,
                    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
                    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
                )
            }

            ThemeContrast.HIGH -> {
                darkColorScheme(
                    primary = primaryDarkHighContrast,
                    onPrimary = onPrimaryDarkHighContrast,
                    primaryContainer = primaryContainerDarkHighContrast,
                    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
                    secondary = secondaryDarkHighContrast,
                    onSecondary = onSecondaryDarkHighContrast,
                    secondaryContainer = secondaryContainerDarkHighContrast,
                    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
                    tertiary = tertiaryDarkHighContrast,
                    onTertiary = onTertiaryDarkHighContrast,
                    tertiaryContainer = tertiaryContainerDarkHighContrast,
                    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
                    error = errorDarkHighContrast,
                    onError = onErrorDarkHighContrast,
                    errorContainer = errorContainerDarkHighContrast,
                    onErrorContainer = onErrorContainerDarkHighContrast,
                    background = backgroundDarkHighContrast,
                    onBackground = onBackgroundDarkHighContrast,
                    surface = surfaceDarkHighContrast,
                    onSurface = onSurfaceDarkHighContrast,
                    surfaceVariant = surfaceVariantDarkHighContrast,
                    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
                    outline = outlineDarkHighContrast,
                    outlineVariant = outlineVariantDarkHighContrast,
                    scrim = scrimDarkHighContrast,
                    inverseSurface = inverseSurfaceDarkHighContrast,
                    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
                    inversePrimary = inversePrimaryDarkHighContrast,
                    surfaceDim = surfaceDimDarkHighContrast,
                    surfaceBright = surfaceBrightDarkHighContrast,
                    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
                    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
                    surfaceContainer = surfaceContainerDarkHighContrast,
                    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
                    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
                )
            }
        }
    } else {
        when (themeContrast) {
            ThemeContrast.STANDARD -> {
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

            ThemeContrast.MEDIUM -> {
                lightColorScheme(
                    primary = primaryLightMediumContrast,
                    onPrimary = onPrimaryLightMediumContrast,
                    primaryContainer = primaryContainerLightMediumContrast,
                    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
                    secondary = secondaryLightMediumContrast,
                    onSecondary = onSecondaryLightMediumContrast,
                    secondaryContainer = secondaryContainerLightMediumContrast,
                    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
                    tertiary = tertiaryLightMediumContrast,
                    onTertiary = onTertiaryLightMediumContrast,
                    tertiaryContainer = tertiaryContainerLightMediumContrast,
                    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
                    error = errorLightMediumContrast,
                    onError = onErrorLightMediumContrast,
                    errorContainer = errorContainerLightMediumContrast,
                    onErrorContainer = onErrorContainerLightMediumContrast,
                    background = backgroundLightMediumContrast,
                    onBackground = onBackgroundLightMediumContrast,
                    surface = surfaceLightMediumContrast,
                    onSurface = onSurfaceLightMediumContrast,
                    surfaceVariant = surfaceVariantLightMediumContrast,
                    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
                    outline = outlineLightMediumContrast,
                    outlineVariant = outlineVariantLightMediumContrast,
                    scrim = scrimLightMediumContrast,
                    inverseSurface = inverseSurfaceLightMediumContrast,
                    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
                    inversePrimary = inversePrimaryLightMediumContrast,
                    surfaceDim = surfaceDimLightMediumContrast,
                    surfaceBright = surfaceBrightLightMediumContrast,
                    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
                    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
                    surfaceContainer = surfaceContainerLightMediumContrast,
                    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
                    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
                )
            }

            ThemeContrast.HIGH -> {
                lightColorScheme(
                    primary = primaryLightHighContrast,
                    onPrimary = onPrimaryLightHighContrast,
                    primaryContainer = primaryContainerLightHighContrast,
                    onPrimaryContainer = onPrimaryContainerLightHighContrast,
                    secondary = secondaryLightHighContrast,
                    onSecondary = onSecondaryLightHighContrast,
                    secondaryContainer = secondaryContainerLightHighContrast,
                    onSecondaryContainer = onSecondaryContainerLightHighContrast,
                    tertiary = tertiaryLightHighContrast,
                    onTertiary = onTertiaryLightHighContrast,
                    tertiaryContainer = tertiaryContainerLightHighContrast,
                    onTertiaryContainer = onTertiaryContainerLightHighContrast,
                    error = errorLightHighContrast,
                    onError = onErrorLightHighContrast,
                    errorContainer = errorContainerLightHighContrast,
                    onErrorContainer = onErrorContainerLightHighContrast,
                    background = backgroundLightHighContrast,
                    onBackground = onBackgroundLightHighContrast,
                    surface = surfaceLightHighContrast,
                    onSurface = onSurfaceLightHighContrast,
                    surfaceVariant = surfaceVariantLightHighContrast,
                    onSurfaceVariant = onSurfaceVariantLightHighContrast,
                    outline = outlineLightHighContrast,
                    outlineVariant = outlineVariantLightHighContrast,
                    scrim = scrimLightHighContrast,
                    inverseSurface = inverseSurfaceLightHighContrast,
                    inverseOnSurface = inverseOnSurfaceLightHighContrast,
                    inversePrimary = inversePrimaryLightHighContrast,
                    surfaceDim = surfaceDimLightHighContrast,
                    surfaceBright = surfaceBrightLightHighContrast,
                    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
                    surfaceContainerLow = surfaceContainerLowLightHighContrast,
                    surfaceContainer = surfaceContainerLightHighContrast,
                    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
                    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
                )
            }
        }
    }
}