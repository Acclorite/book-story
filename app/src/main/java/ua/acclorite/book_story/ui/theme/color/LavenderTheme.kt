package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.theme.ThemeContrast

private val primaryLight = Color(0xFF824C76)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFFFD7F1)
private val onPrimaryContainerLight = Color(0xFF35082F)
private val secondaryLight = Color(0xFF6F5867)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFF9DAED)
private val onSecondaryContainerLight = Color(0xFF271623)
private val tertiaryLight = Color(0xFF815342)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFFFDBCF)
private val onTertiaryContainerLight = Color(0xFF321206)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFFFF7F9)
private val onBackgroundLight = Color(0xFF201A1E)
private val surfaceLight = Color(0xFFFFF7F9)
private val onSurfaceLight = Color(0xFF201A1E)
private val surfaceVariantLight = Color(0xFFEFDEE6)
private val onSurfaceVariantLight = Color(0xFF4E444A)
private val outlineLight = Color(0xFF80747B)
private val outlineVariantLight = Color(0xFFD2C2CA)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF362E33)
private val inverseOnSurfaceLight = Color(0xFFFBEDF3)
private val inversePrimaryLight = Color(0xFFF4B2E2)
private val surfaceDimLight = Color(0xFFE3D7DC)
private val surfaceBrightLight = Color(0xFFFFF7F9)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFFEF0F6)
private val surfaceContainerLight = Color(0xFFF8EAF0)
private val surfaceContainerHighLight = Color(0xFFF2E5EB)
private val surfaceContainerHighestLight = Color(0xFFECDFE5)

private val primaryLightMediumContrast = Color(0xFF633159)
private val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
private val primaryContainerLightMediumContrast = Color(0xFF9B628D)
private val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryLightMediumContrast = Color(0xFF523C4B)
private val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightMediumContrast = Color(0xFF866D7E)
private val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryLightMediumContrast = Color(0xFF613829)
private val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightMediumContrast = Color(0xFF9A6957)
private val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val errorLightMediumContrast = Color(0xFF8C0009)
private val onErrorLightMediumContrast = Color(0xFFFFFFFF)
private val errorContainerLightMediumContrast = Color(0xFFDA342E)
private val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
private val backgroundLightMediumContrast = Color(0xFFFFF7F9)
private val onBackgroundLightMediumContrast = Color(0xFF201A1E)
private val surfaceLightMediumContrast = Color(0xFFFFF7F9)
private val onSurfaceLightMediumContrast = Color(0xFF201A1E)
private val surfaceVariantLightMediumContrast = Color(0xFFEFDEE6)
private val onSurfaceVariantLightMediumContrast = Color(0xFF4A4046)
private val outlineLightMediumContrast = Color(0xFF685C63)
private val outlineVariantLightMediumContrast = Color(0xFF84777F)
private val scrimLightMediumContrast = Color(0xFF000000)
private val inverseSurfaceLightMediumContrast = Color(0xFF362E33)
private val inverseOnSurfaceLightMediumContrast = Color(0xFFFBEDF3)
private val inversePrimaryLightMediumContrast = Color(0xFFF4B2E2)
private val surfaceDimLightMediumContrast = Color(0xFFE3D7DC)
private val surfaceBrightLightMediumContrast = Color(0xFFFFF7F9)
private val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightMediumContrast = Color(0xFFFEF0F6)
private val surfaceContainerLightMediumContrast = Color(0xFFF8EAF0)
private val surfaceContainerHighLightMediumContrast = Color(0xFFF2E5EB)
private val surfaceContainerHighestLightMediumContrast = Color(0xFFECDFE5)

private val primaryLightHighContrast = Color(0xFF3D0F36)
private val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
private val primaryContainerLightHighContrast = Color(0xFF633159)
private val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val secondaryLightHighContrast = Color(0xFF2F1C2A)
private val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightHighContrast = Color(0xFF523C4B)
private val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryLightHighContrast = Color(0xFF3A190B)
private val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightHighContrast = Color(0xFF613829)
private val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val errorLightHighContrast = Color(0xFF4E0002)
private val onErrorLightHighContrast = Color(0xFFFFFFFF)
private val errorContainerLightHighContrast = Color(0xFF8C0009)
private val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
private val backgroundLightHighContrast = Color(0xFFFFF7F9)
private val onBackgroundLightHighContrast = Color(0xFF201A1E)
private val surfaceLightHighContrast = Color(0xFFFFF7F9)
private val onSurfaceLightHighContrast = Color(0xFF000000)
private val surfaceVariantLightHighContrast = Color(0xFFEFDEE6)
private val onSurfaceVariantLightHighContrast = Color(0xFF2A2127)
private val outlineLightHighContrast = Color(0xFF4A4046)
private val outlineVariantLightHighContrast = Color(0xFF4A4046)
private val scrimLightHighContrast = Color(0xFF000000)
private val inverseSurfaceLightHighContrast = Color(0xFF362E33)
private val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
private val inversePrimaryLightHighContrast = Color(0xFFFFE5F4)
private val surfaceDimLightHighContrast = Color(0xFFE3D7DC)
private val surfaceBrightLightHighContrast = Color(0xFFFFF7F9)
private val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightHighContrast = Color(0xFFFEF0F6)
private val surfaceContainerLightHighContrast = Color(0xFFF8EAF0)
private val surfaceContainerHighLightHighContrast = Color(0xFFF2E5EB)
private val surfaceContainerHighestLightHighContrast = Color(0xFFECDFE5)

private val primaryDark = Color(0xFFF4B2E2)
private val onPrimaryDark = Color(0xFF4E1E46)
private val primaryContainerDark = Color(0xFF67355D)
private val onPrimaryContainerDark = Color(0xFFFFD7F1)
private val secondaryDark = Color(0xFFDCBED1)
private val onSecondaryDark = Color(0xFF3E2A39)
private val secondaryContainerDark = Color(0xFF56404F)
private val onSecondaryContainerDark = Color(0xFFF9DAED)
private val tertiaryDark = Color(0xFFF5B9A4)
private val onTertiaryDark = Color(0xFF4C2618)
private val tertiaryContainerDark = Color(0xFF663C2C)
private val onTertiaryContainerDark = Color(0xFFFFDBCF)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF181216)
private val onBackgroundDark = Color(0xFFECDFE5)
private val surfaceDark = Color(0xFF181216)
private val onSurfaceDark = Color(0xFFECDFE5)
private val surfaceVariantDark = Color(0xFF4E444A)
private val onSurfaceVariantDark = Color(0xFFD2C2CA)
private val outlineDark = Color(0xFF9B8D95)
private val outlineVariantDark = Color(0xFF4E444A)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFECDFE5)
private val inverseOnSurfaceDark = Color(0xFF362E33)
private val inversePrimaryDark = Color(0xFF824C76)
private val surfaceDimDark = Color(0xFF181216)
private val surfaceBrightDark = Color(0xFF3F373C)
private val surfaceContainerLowestDark = Color(0xFF120C10)
private val surfaceContainerLowDark = Color(0xFF201A1E)
private val surfaceContainerDark = Color(0xFF241E22)
private val surfaceContainerHighDark = Color(0xFF2F282C)
private val surfaceContainerHighestDark = Color(0xFF3A3337)

private val primaryDarkMediumContrast = Color(0xFFF9B6E7)
private val onPrimaryDarkMediumContrast = Color(0xFF2E032A)
private val primaryContainerDarkMediumContrast = Color(0xFFBA7EAB)
private val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
private val secondaryDarkMediumContrast = Color(0xFFE0C2D5)
private val onSecondaryDarkMediumContrast = Color(0xFF22111E)
private val secondaryContainerDarkMediumContrast = Color(0xFFA3899A)
private val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
private val tertiaryDarkMediumContrast = Color(0xFFFABDA8)
private val onTertiaryDarkMediumContrast = Color(0xFF2B0D03)
private val tertiaryContainerDarkMediumContrast = Color(0xFFBA8471)
private val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
private val errorDarkMediumContrast = Color(0xFFFFBAB1)
private val onErrorDarkMediumContrast = Color(0xFF370001)
private val errorContainerDarkMediumContrast = Color(0xFFFF5449)
private val onErrorContainerDarkMediumContrast = Color(0xFF000000)
private val backgroundDarkMediumContrast = Color(0xFF181216)
private val onBackgroundDarkMediumContrast = Color(0xFFECDFE5)
private val surfaceDarkMediumContrast = Color(0xFF181216)
private val onSurfaceDarkMediumContrast = Color(0xFFFFF9FA)
private val surfaceVariantDarkMediumContrast = Color(0xFF4E444A)
private val onSurfaceVariantDarkMediumContrast = Color(0xFFD6C7CF)
private val outlineDarkMediumContrast = Color(0xFFAD9FA7)
private val outlineVariantDarkMediumContrast = Color(0xFF8D7F87)
private val scrimDarkMediumContrast = Color(0xFF000000)
private val inverseSurfaceDarkMediumContrast = Color(0xFFECDFE5)
private val inverseOnSurfaceDarkMediumContrast = Color(0xFF2F282C)
private val inversePrimaryDarkMediumContrast = Color(0xFF69365F)
private val surfaceDimDarkMediumContrast = Color(0xFF181216)
private val surfaceBrightDarkMediumContrast = Color(0xFF3F373C)
private val surfaceContainerLowestDarkMediumContrast = Color(0xFF120C10)
private val surfaceContainerLowDarkMediumContrast = Color(0xFF201A1E)
private val surfaceContainerDarkMediumContrast = Color(0xFF241E22)
private val surfaceContainerHighDarkMediumContrast = Color(0xFF2F282C)
private val surfaceContainerHighestDarkMediumContrast = Color(0xFF3A3337)

private val primaryDarkHighContrast = Color(0xFFFFF9FA)
private val onPrimaryDarkHighContrast = Color(0xFF000000)
private val primaryContainerDarkHighContrast = Color(0xFFF9B6E7)
private val onPrimaryContainerDarkHighContrast = Color(0xFF000000)
private val secondaryDarkHighContrast = Color(0xFFFFF9FA)
private val onSecondaryDarkHighContrast = Color(0xFF000000)
private val secondaryContainerDarkHighContrast = Color(0xFFE0C2D5)
private val onSecondaryContainerDarkHighContrast = Color(0xFF000000)
private val tertiaryDarkHighContrast = Color(0xFFFFF9F8)
private val onTertiaryDarkHighContrast = Color(0xFF000000)
private val tertiaryContainerDarkHighContrast = Color(0xFFFABDA8)
private val onTertiaryContainerDarkHighContrast = Color(0xFF000000)
private val errorDarkHighContrast = Color(0xFFFFF9F9)
private val onErrorDarkHighContrast = Color(0xFF000000)
private val errorContainerDarkHighContrast = Color(0xFFFFBAB1)
private val onErrorContainerDarkHighContrast = Color(0xFF000000)
private val backgroundDarkHighContrast = Color(0xFF181216)
private val onBackgroundDarkHighContrast = Color(0xFFECDFE5)
private val surfaceDarkHighContrast = Color(0xFF181216)
private val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
private val surfaceVariantDarkHighContrast = Color(0xFF4E444A)
private val onSurfaceVariantDarkHighContrast = Color(0xFFFFF9FA)
private val outlineDarkHighContrast = Color(0xFFD6C7CF)
private val outlineVariantDarkHighContrast = Color(0xFFD6C7CF)
private val scrimDarkHighContrast = Color(0xFF000000)
private val inverseSurfaceDarkHighContrast = Color(0xFFECDFE5)
private val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
private val inversePrimaryDarkHighContrast = Color(0xFF46183F)
private val surfaceDimDarkHighContrast = Color(0xFF181216)
private val surfaceBrightDarkHighContrast = Color(0xFF3F373C)
private val surfaceContainerLowestDarkHighContrast = Color(0xFF120C10)
private val surfaceContainerLowDarkHighContrast = Color(0xFF201A1E)
private val surfaceContainerDarkHighContrast = Color(0xFF241E22)
private val surfaceContainerHighDarkHighContrast = Color(0xFF2F282C)
private val surfaceContainerHighestDarkHighContrast = Color(0xFF3A3337)


@Composable
fun lavenderTheme(isDark: Boolean, themeContrast: ThemeContrast): ColorScheme {
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