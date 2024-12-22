package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.theme.ThemeContrast


private val primaryLight = Color(0xFF116682)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFBDE9FF)
private val onPrimaryContainerLight = Color(0xFF001F2A)
private val secondaryLight = Color(0xFF4D616C)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFD0E6F2)
private val onSecondaryContainerLight = Color(0xFF081E27)
private val tertiaryLight = Color(0xFF5D5B7D)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFE3DFFF)
private val onTertiaryContainerLight = Color(0xFF191836)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFF6FAFD)
private val onBackgroundLight = Color(0xFF171C1F)
private val surfaceLight = Color(0xFFF6FAFD)
private val onSurfaceLight = Color(0xFF171C1F)
private val surfaceVariantLight = Color(0xFFDCE4E9)
private val onSurfaceVariantLight = Color(0xFF40484C)
private val outlineLight = Color(0xFF70787D)
private val outlineVariantLight = Color(0xFFC0C8CD)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF2C3134)
private val inverseOnSurfaceLight = Color(0xFFEDF1F5)
private val inversePrimaryLight = Color(0xFF8BD0EF)
private val surfaceDimLight = Color(0xFFD6DBDE)
private val surfaceBrightLight = Color(0xFFF6FAFD)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF0F4F8)
private val surfaceContainerLight = Color(0xFFEAEEF2)
private val surfaceContainerHighLight = Color(0xFFE4E9EC)
private val surfaceContainerHighestLight = Color(0xFFDFE3E7)

private val primaryLightMediumContrast = Color(0xFF00495F)
private val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
private val primaryContainerLightMediumContrast = Color(0xFF337D99)
private val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryLightMediumContrast = Color(0xFF31464F)
private val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightMediumContrast = Color(0xFF637882)
private val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryLightMediumContrast = Color(0xFF413F60)
private val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightMediumContrast = Color(0xFF737195)
private val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val errorLightMediumContrast = Color(0xFF8C0009)
private val onErrorLightMediumContrast = Color(0xFFFFFFFF)
private val errorContainerLightMediumContrast = Color(0xFFDA342E)
private val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
private val backgroundLightMediumContrast = Color(0xFFF6FAFD)
private val onBackgroundLightMediumContrast = Color(0xFF171C1F)
private val surfaceLightMediumContrast = Color(0xFFF6FAFD)
private val onSurfaceLightMediumContrast = Color(0xFF171C1F)
private val surfaceVariantLightMediumContrast = Color(0xFFDCE4E9)
private val onSurfaceVariantLightMediumContrast = Color(0xFF3C4448)
private val outlineLightMediumContrast = Color(0xFF586065)
private val outlineVariantLightMediumContrast = Color(0xFF747C80)
private val scrimLightMediumContrast = Color(0xFF000000)
private val inverseSurfaceLightMediumContrast = Color(0xFF2C3134)
private val inverseOnSurfaceLightMediumContrast = Color(0xFFEDF1F5)
private val inversePrimaryLightMediumContrast = Color(0xFF8BD0EF)
private val surfaceDimLightMediumContrast = Color(0xFFD6DBDE)
private val surfaceBrightLightMediumContrast = Color(0xFFF6FAFD)
private val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightMediumContrast = Color(0xFFF0F4F8)
private val surfaceContainerLightMediumContrast = Color(0xFFEAEEF2)
private val surfaceContainerHighLightMediumContrast = Color(0xFFE4E9EC)
private val surfaceContainerHighestLightMediumContrast = Color(0xFFDFE3E7)

private val primaryLightHighContrast = Color(0xFF002633)
private val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
private val primaryContainerLightHighContrast = Color(0xFF00495F)
private val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val secondaryLightHighContrast = Color(0xFF10252E)
private val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightHighContrast = Color(0xFF31464F)
private val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryLightHighContrast = Color(0xFF201F3D)
private val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightHighContrast = Color(0xFF413F60)
private val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val errorLightHighContrast = Color(0xFF4E0002)
private val onErrorLightHighContrast = Color(0xFFFFFFFF)
private val errorContainerLightHighContrast = Color(0xFF8C0009)
private val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
private val backgroundLightHighContrast = Color(0xFFF6FAFD)
private val onBackgroundLightHighContrast = Color(0xFF171C1F)
private val surfaceLightHighContrast = Color(0xFFF6FAFD)
private val onSurfaceLightHighContrast = Color(0xFF000000)
private val surfaceVariantLightHighContrast = Color(0xFFDCE4E9)
private val onSurfaceVariantLightHighContrast = Color(0xFF1D2529)
private val outlineLightHighContrast = Color(0xFF3C4448)
private val outlineVariantLightHighContrast = Color(0xFF3C4448)
private val scrimLightHighContrast = Color(0xFF000000)
private val inverseSurfaceLightHighContrast = Color(0xFF2C3134)
private val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
private val inversePrimaryLightHighContrast = Color(0xFFD5F0FF)
private val surfaceDimLightHighContrast = Color(0xFFD6DBDE)
private val surfaceBrightLightHighContrast = Color(0xFFF6FAFD)
private val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightHighContrast = Color(0xFFF0F4F8)
private val surfaceContainerLightHighContrast = Color(0xFFEAEEF2)
private val surfaceContainerHighLightHighContrast = Color(0xFFE4E9EC)
private val surfaceContainerHighestLightHighContrast = Color(0xFFDFE3E7)


private val primaryDark = Color(0xFF8BD0EF)
private val onPrimaryDark = Color(0xFF003546)
private val primaryContainerDark = Color(0xFF004D64)
private val onPrimaryContainerDark = Color(0xFFBDE9FF)
private val secondaryDark = Color(0xFFB4CAD6)
private val onSecondaryDark = Color(0xFF1F333C)
private val secondaryContainerDark = Color(0xFF354A53)
private val onSecondaryContainerDark = Color(0xFFD0E6F2)
private val tertiaryDark = Color(0xFFC6C2EA)
private val onTertiaryDark = Color(0xFF2E2D4D)
private val tertiaryContainerDark = Color(0xFF454364)
private val onTertiaryContainerDark = Color(0xFFE3DFFF)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF0F1417)
private val onBackgroundDark = Color(0xFFDFE3E7)
private val surfaceDark = Color(0xFF0F1417)
private val onSurfaceDark = Color(0xFFDFE3E7)
private val surfaceVariantDark = Color(0xFF40484C)
private val onSurfaceVariantDark = Color(0xFFC0C8CD)
private val outlineDark = Color(0xFF8A9297)
private val outlineVariantDark = Color(0xFF40484C)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFDFE3E7)
private val inverseOnSurfaceDark = Color(0xFF2C3134)
private val inversePrimaryDark = Color(0xFF116682)
private val surfaceDimDark = Color(0xFF0F1417)
private val surfaceBrightDark = Color(0xFF353A3D)
private val surfaceContainerLowestDark = Color(0xFF0A0F11)
private val surfaceContainerLowDark = Color(0xFF171C1F)
private val surfaceContainerDark = Color(0xFF1B2023)
private val surfaceContainerHighDark = Color(0xFF262B2D)
private val surfaceContainerHighestDark = Color(0xFF303538)

private val primaryDarkMediumContrast = Color(0xFF8FD4F4)
private val onPrimaryDarkMediumContrast = Color(0xFF001923)
private val primaryContainerDarkMediumContrast = Color(0xFF5399B7)
private val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
private val secondaryDarkMediumContrast = Color(0xFFB8CEDA)
private val onSecondaryDarkMediumContrast = Color(0xFF031921)
private val secondaryContainerDarkMediumContrast = Color(0xFF7F949F)
private val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
private val tertiaryDarkMediumContrast = Color(0xFFCAC7EF)
private val onTertiaryDarkMediumContrast = Color(0xFF141231)
private val tertiaryContainerDarkMediumContrast = Color(0xFF908DB2)
private val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
private val errorDarkMediumContrast = Color(0xFFFFBAB1)
private val onErrorDarkMediumContrast = Color(0xFF370001)
private val errorContainerDarkMediumContrast = Color(0xFFFF5449)
private val onErrorContainerDarkMediumContrast = Color(0xFF000000)
private val backgroundDarkMediumContrast = Color(0xFF0F1417)
private val onBackgroundDarkMediumContrast = Color(0xFFDFE3E7)
private val surfaceDarkMediumContrast = Color(0xFF0F1417)
private val onSurfaceDarkMediumContrast = Color(0xFFF7FBFF)
private val surfaceVariantDarkMediumContrast = Color(0xFF40484C)
private val onSurfaceVariantDarkMediumContrast = Color(0xFFC4CCD1)
private val outlineDarkMediumContrast = Color(0xFF9CA4A9)
private val outlineVariantDarkMediumContrast = Color(0xFF7C8489)
private val scrimDarkMediumContrast = Color(0xFF000000)
private val inverseSurfaceDarkMediumContrast = Color(0xFFDFE3E7)
private val inverseOnSurfaceDarkMediumContrast = Color(0xFF262B2E)
private val inversePrimaryDarkMediumContrast = Color(0xFF004E66)
private val surfaceDimDarkMediumContrast = Color(0xFF0F1417)
private val surfaceBrightDarkMediumContrast = Color(0xFF353A3D)
private val surfaceContainerLowestDarkMediumContrast = Color(0xFF0A0F11)
private val surfaceContainerLowDarkMediumContrast = Color(0xFF171C1F)
private val surfaceContainerDarkMediumContrast = Color(0xFF1B2023)
private val surfaceContainerHighDarkMediumContrast = Color(0xFF262B2D)
private val surfaceContainerHighestDarkMediumContrast = Color(0xFF303538)

private val primaryDarkHighContrast = Color(0xFFF7FBFF)
private val onPrimaryDarkHighContrast = Color(0xFF000000)
private val primaryContainerDarkHighContrast = Color(0xFF8FD4F4)
private val onPrimaryContainerDarkHighContrast = Color(0xFF000000)
private val secondaryDarkHighContrast = Color(0xFFF7FBFF)
private val onSecondaryDarkHighContrast = Color(0xFF000000)
private val secondaryContainerDarkHighContrast = Color(0xFFB8CEDA)
private val onSecondaryContainerDarkHighContrast = Color(0xFF000000)
private val tertiaryDarkHighContrast = Color(0xFFFEF9FF)
private val onTertiaryDarkHighContrast = Color(0xFF000000)
private val tertiaryContainerDarkHighContrast = Color(0xFFCAC7EF)
private val onTertiaryContainerDarkHighContrast = Color(0xFF000000)
private val errorDarkHighContrast = Color(0xFFFFF9F9)
private val onErrorDarkHighContrast = Color(0xFF000000)
private val errorContainerDarkHighContrast = Color(0xFFFFBAB1)
private val onErrorContainerDarkHighContrast = Color(0xFF000000)
private val backgroundDarkHighContrast = Color(0xFF0F1417)
private val onBackgroundDarkHighContrast = Color(0xFFDFE3E7)
private val surfaceDarkHighContrast = Color(0xFF0F1417)
private val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
private val surfaceVariantDarkHighContrast = Color(0xFF40484C)
private val onSurfaceVariantDarkHighContrast = Color(0xFFF7FBFF)
private val outlineDarkHighContrast = Color(0xFFC4CCD1)
private val outlineVariantDarkHighContrast = Color(0xFFC4CCD1)
private val scrimDarkHighContrast = Color(0xFF000000)
private val inverseSurfaceDarkHighContrast = Color(0xFFDFE3E7)
private val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
private val inversePrimaryDarkHighContrast = Color(0xFF002E3D)
private val surfaceDimDarkHighContrast = Color(0xFF0F1417)
private val surfaceBrightDarkHighContrast = Color(0xFF353A3D)
private val surfaceContainerLowestDarkHighContrast = Color(0xFF0A0F11)
private val surfaceContainerLowDarkHighContrast = Color(0xFF171C1F)
private val surfaceContainerDarkHighContrast = Color(0xFF1B2023)
private val surfaceContainerHighDarkHighContrast = Color(0xFF262B2D)
private val surfaceContainerHighestDarkHighContrast = Color(0xFF303538)


@Composable
fun aquaTheme(isDark: Boolean, themeContrast: ThemeContrast): ColorScheme {
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