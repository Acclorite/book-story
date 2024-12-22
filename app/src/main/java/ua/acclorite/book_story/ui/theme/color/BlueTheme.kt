package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.theme.ThemeContrast


private val primaryLight = Color(0xFF485D92)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFDAE2FF)
private val onPrimaryContainerLight = Color(0xFF001847)
private val secondaryLight = Color(0xFF585E71)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFDCE2F9)
private val onSecondaryContainerLight = Color(0xFF151B2C)
private val tertiaryLight = Color(0xFF735572)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFFED7F9)
private val onTertiaryContainerLight = Color(0xFF2A122C)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFFAF8FF)
private val onBackgroundLight = Color(0xFF1A1B21)
private val surfaceLight = Color(0xFFFAF8FF)
private val onSurfaceLight = Color(0xFF1A1B21)
private val surfaceVariantLight = Color(0xFFE1E2EC)
private val onSurfaceVariantLight = Color(0xFF44464F)
private val outlineLight = Color(0xFF757780)
private val outlineVariantLight = Color(0xFFC5C6D0)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF2F3036)
private val inverseOnSurfaceLight = Color(0xFFF1F0F7)
private val inversePrimaryLight = Color(0xFFB1C5FF)
private val surfaceDimLight = Color(0xFFDAD9E0)
private val surfaceBrightLight = Color(0xFFFAF8FF)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF4F3FA)
private val surfaceContainerLight = Color(0xFFEEEDF4)
private val surfaceContainerHighLight = Color(0xFFE8E7EF)
private val surfaceContainerHighestLight = Color(0xFFE2E2E9)

private val primaryLightMediumContrast = Color(0xFF2C4174)
private val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
private val primaryContainerLightMediumContrast = Color(0xFF5F73AA)
private val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryLightMediumContrast = Color(0xFF3C4255)
private val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightMediumContrast = Color(0xFF6E7488)
private val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryLightMediumContrast = Color(0xFF553955)
private val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightMediumContrast = Color(0xFF8A6A88)
private val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val errorLightMediumContrast = Color(0xFF8C0009)
private val onErrorLightMediumContrast = Color(0xFFFFFFFF)
private val errorContainerLightMediumContrast = Color(0xFFDA342E)
private val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
private val backgroundLightMediumContrast = Color(0xFFFAF8FF)
private val onBackgroundLightMediumContrast = Color(0xFF1A1B21)
private val surfaceLightMediumContrast = Color(0xFFFAF8FF)
private val onSurfaceLightMediumContrast = Color(0xFF1A1B21)
private val surfaceVariantLightMediumContrast = Color(0xFFE1E2EC)
private val onSurfaceVariantLightMediumContrast = Color(0xFF41424B)
private val outlineLightMediumContrast = Color(0xFF5D5F67)
private val outlineVariantLightMediumContrast = Color(0xFF797A83)
private val scrimLightMediumContrast = Color(0xFF000000)
private val inverseSurfaceLightMediumContrast = Color(0xFF2F3036)
private val inverseOnSurfaceLightMediumContrast = Color(0xFFF1F0F7)
private val inversePrimaryLightMediumContrast = Color(0xFFB1C5FF)
private val surfaceDimLightMediumContrast = Color(0xFFDAD9E0)
private val surfaceBrightLightMediumContrast = Color(0xFFFAF8FF)
private val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightMediumContrast = Color(0xFFF4F3FA)
private val surfaceContainerLightMediumContrast = Color(0xFFEEEDF4)
private val surfaceContainerHighLightMediumContrast = Color(0xFFE8E7EF)
private val surfaceContainerHighestLightMediumContrast = Color(0xFFE2E2E9)

private val primaryLightHighContrast = Color(0xFF041F51)
private val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
private val primaryContainerLightHighContrast = Color(0xFF2C4174)
private val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val secondaryLightHighContrast = Color(0xFF1C2233)
private val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightHighContrast = Color(0xFF3C4255)
private val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryLightHighContrast = Color(0xFF321933)
private val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightHighContrast = Color(0xFF553955)
private val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val errorLightHighContrast = Color(0xFF4E0002)
private val onErrorLightHighContrast = Color(0xFFFFFFFF)
private val errorContainerLightHighContrast = Color(0xFF8C0009)
private val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
private val backgroundLightHighContrast = Color(0xFFFAF8FF)
private val onBackgroundLightHighContrast = Color(0xFF1A1B21)
private val surfaceLightHighContrast = Color(0xFFFAF8FF)
private val onSurfaceLightHighContrast = Color(0xFF000000)
private val surfaceVariantLightHighContrast = Color(0xFFE1E2EC)
private val onSurfaceVariantLightHighContrast = Color(0xFF21242B)
private val outlineLightHighContrast = Color(0xFF41424B)
private val outlineVariantLightHighContrast = Color(0xFF41424B)
private val scrimLightHighContrast = Color(0xFF000000)
private val inverseSurfaceLightHighContrast = Color(0xFF2F3036)
private val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
private val inversePrimaryLightHighContrast = Color(0xFFE7EBFF)
private val surfaceDimLightHighContrast = Color(0xFFDAD9E0)
private val surfaceBrightLightHighContrast = Color(0xFFFAF8FF)
private val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightHighContrast = Color(0xFFF4F3FA)
private val surfaceContainerLightHighContrast = Color(0xFFEEEDF4)
private val surfaceContainerHighLightHighContrast = Color(0xFFE8E7EF)
private val surfaceContainerHighestLightHighContrast = Color(0xFFE2E2E9)


private val primaryDark = Color(0xFFB1C5FF)
private val onPrimaryDark = Color(0xFF172E60)
private val primaryContainerDark = Color(0xFF304578)
private val onPrimaryContainerDark = Color(0xFFDAE2FF)
private val secondaryDark = Color(0xFFC0C6DD)
private val onSecondaryDark = Color(0xFF2A3042)
private val secondaryContainerDark = Color(0xFF404659)
private val onSecondaryContainerDark = Color(0xFFDCE2F9)
private val tertiaryDark = Color(0xFFE1BBDD)
private val onTertiaryDark = Color(0xFF412742)
private val tertiaryContainerDark = Color(0xFF5A3D59)
private val onTertiaryContainerDark = Color(0xFFFED7F9)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF121318)
private val onBackgroundDark = Color(0xFFE2E2E9)
private val surfaceDark = Color(0xFF121318)
private val onSurfaceDark = Color(0xFFE2E2E9)
private val surfaceVariantDark = Color(0xFF44464F)
private val onSurfaceVariantDark = Color(0xFFC5C6D0)
private val outlineDark = Color(0xFF8F9099)
private val outlineVariantDark = Color(0xFF44464F)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFE2E2E9)
private val inverseOnSurfaceDark = Color(0xFF2F3036)
private val inversePrimaryDark = Color(0xFF485D92)
private val surfaceDimDark = Color(0xFF121318)
private val surfaceBrightDark = Color(0xFF38393F)
private val surfaceContainerLowestDark = Color(0xFF0D0E13)
private val surfaceContainerLowDark = Color(0xFF1A1B21)
private val surfaceContainerDark = Color(0xFF1E1F25)
private val surfaceContainerHighDark = Color(0xFF282A2F)
private val surfaceContainerHighestDark = Color(0xFF33343A)

private val primaryDarkMediumContrast = Color(0xFFB8CAFF)
private val onPrimaryDarkMediumContrast = Color(0xFF00143C)
private val primaryContainerDarkMediumContrast = Color(0xFF7B8FC8)
private val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
private val secondaryDarkMediumContrast = Color(0xFFC4CAE1)
private val onSecondaryDarkMediumContrast = Color(0xFF0F1626)
private val secondaryContainerDarkMediumContrast = Color(0xFF8A90A5)
private val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
private val tertiaryDarkMediumContrast = Color(0xFFE5BFE1)
private val onTertiaryDarkMediumContrast = Color(0xFF250D26)
private val tertiaryContainerDarkMediumContrast = Color(0xFFA886A6)
private val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
private val errorDarkMediumContrast = Color(0xFFFFBAB1)
private val onErrorDarkMediumContrast = Color(0xFF370001)
private val errorContainerDarkMediumContrast = Color(0xFFFF5449)
private val onErrorContainerDarkMediumContrast = Color(0xFF000000)
private val backgroundDarkMediumContrast = Color(0xFF121318)
private val onBackgroundDarkMediumContrast = Color(0xFFE2E2E9)
private val surfaceDarkMediumContrast = Color(0xFF121318)
private val onSurfaceDarkMediumContrast = Color(0xFFFCFAFF)
private val surfaceVariantDarkMediumContrast = Color(0xFF44464F)
private val onSurfaceVariantDarkMediumContrast = Color(0xFFC9CAD4)
private val outlineDarkMediumContrast = Color(0xFFA1A2AC)
private val outlineVariantDarkMediumContrast = Color(0xFF81838C)
private val scrimDarkMediumContrast = Color(0xFF000000)
private val inverseSurfaceDarkMediumContrast = Color(0xFFE2E2E9)
private val inverseOnSurfaceDarkMediumContrast = Color(0xFF282A2F)
private val inversePrimaryDarkMediumContrast = Color(0xFF31467A)
private val surfaceDimDarkMediumContrast = Color(0xFF121318)
private val surfaceBrightDarkMediumContrast = Color(0xFF38393F)
private val surfaceContainerLowestDarkMediumContrast = Color(0xFF0D0E13)
private val surfaceContainerLowDarkMediumContrast = Color(0xFF1A1B21)
private val surfaceContainerDarkMediumContrast = Color(0xFF1E1F25)
private val surfaceContainerHighDarkMediumContrast = Color(0xFF282A2F)
private val surfaceContainerHighestDarkMediumContrast = Color(0xFF33343A)

private val primaryDarkHighContrast = Color(0xFFFCFAFF)
private val onPrimaryDarkHighContrast = Color(0xFF000000)
private val primaryContainerDarkHighContrast = Color(0xFFB8CAFF)
private val onPrimaryContainerDarkHighContrast = Color(0xFF000000)
private val secondaryDarkHighContrast = Color(0xFFFCFAFF)
private val onSecondaryDarkHighContrast = Color(0xFF000000)
private val secondaryContainerDarkHighContrast = Color(0xFFC4CAE1)
private val onSecondaryContainerDarkHighContrast = Color(0xFF000000)
private val tertiaryDarkHighContrast = Color(0xFFFFF9FA)
private val onTertiaryDarkHighContrast = Color(0xFF000000)
private val tertiaryContainerDarkHighContrast = Color(0xFFE5BFE1)
private val onTertiaryContainerDarkHighContrast = Color(0xFF000000)
private val errorDarkHighContrast = Color(0xFFFFF9F9)
private val onErrorDarkHighContrast = Color(0xFF000000)
private val errorContainerDarkHighContrast = Color(0xFFFFBAB1)
private val onErrorContainerDarkHighContrast = Color(0xFF000000)
private val backgroundDarkHighContrast = Color(0xFF121318)
private val onBackgroundDarkHighContrast = Color(0xFFE2E2E9)
private val surfaceDarkHighContrast = Color(0xFF121318)
private val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
private val surfaceVariantDarkHighContrast = Color(0xFF44464F)
private val onSurfaceVariantDarkHighContrast = Color(0xFFFCFAFF)
private val outlineDarkHighContrast = Color(0xFFC9CAD4)
private val outlineVariantDarkHighContrast = Color(0xFFC9CAD4)
private val scrimDarkHighContrast = Color(0xFF000000)
private val inverseSurfaceDarkHighContrast = Color(0xFFE2E2E9)
private val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
private val inversePrimaryDarkHighContrast = Color(0xFF0F275A)
private val surfaceDimDarkHighContrast = Color(0xFF121318)
private val surfaceBrightDarkHighContrast = Color(0xFF38393F)
private val surfaceContainerLowestDarkHighContrast = Color(0xFF0D0E13)
private val surfaceContainerLowDarkHighContrast = Color(0xFF1A1B21)
private val surfaceContainerDarkHighContrast = Color(0xFF1E1F25)
private val surfaceContainerHighDarkHighContrast = Color(0xFF282A2F)
private val surfaceContainerHighestDarkHighContrast = Color(0xFF33343A)

@Composable
fun blueTheme(isDark: Boolean, themeContrast: ThemeContrast): ColorScheme {
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