package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.theme.ThemeContrast

private val primaryLight = Color(0xFF306A42)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFB3F1BF)
private val onPrimaryContainerLight = Color(0xFF00210D)
private val secondaryLight = Color(0xFF506353)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFD2E8D3)
private val onSecondaryContainerLight = Color(0xFF0D1F12)
private val tertiaryLight = Color(0xFF3A656F)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFBEEAF6)
private val onTertiaryContainerLight = Color(0xFF001F25)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFF6FBF3)
private val onBackgroundLight = Color(0xFF181D18)
private val surfaceLight = Color(0xFFF6FBF3)
private val onSurfaceLight = Color(0xFF181D18)
private val surfaceVariantLight = Color(0xFFDDE5DA)
private val onSurfaceVariantLight = Color(0xFF414941)
private val outlineLight = Color(0xFF717971)
private val outlineVariantLight = Color(0xFFC1C9BF)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF2D322D)
private val inverseOnSurfaceLight = Color(0xFFEEF2EA)
private val inversePrimaryLight = Color(0xFF97D5A5)
private val surfaceDimLight = Color(0xFFD7DBD4)
private val surfaceBrightLight = Color(0xFFF6FBF3)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF0F5ED)
private val surfaceContainerLight = Color(0xFFEBEFE7)
private val surfaceContainerHighLight = Color(0xFFE5EAE2)
private val surfaceContainerHighestLight = Color(0xFFDFE4DC)

private val primaryLightMediumContrast = Color(0xFF0F4D29)
private val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
private val primaryContainerLightMediumContrast = Color(0xFF478157)
private val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryLightMediumContrast = Color(0xFF344738)
private val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightMediumContrast = Color(0xFF657A68)
private val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryLightMediumContrast = Color(0xFF1C4952)
private val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightMediumContrast = Color(0xFF517B85)
private val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
private val errorLightMediumContrast = Color(0xFF8C0009)
private val onErrorLightMediumContrast = Color(0xFFFFFFFF)
private val errorContainerLightMediumContrast = Color(0xFFDA342E)
private val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
private val backgroundLightMediumContrast = Color(0xFFF6FBF3)
private val onBackgroundLightMediumContrast = Color(0xFF181D18)
private val surfaceLightMediumContrast = Color(0xFFF6FBF3)
private val onSurfaceLightMediumContrast = Color(0xFF181D18)
private val surfaceVariantLightMediumContrast = Color(0xFFDDE5DA)
private val onSurfaceVariantLightMediumContrast = Color(0xFF3D453D)
private val outlineLightMediumContrast = Color(0xFF596159)
private val outlineVariantLightMediumContrast = Color(0xFF757D74)
private val scrimLightMediumContrast = Color(0xFF000000)
private val inverseSurfaceLightMediumContrast = Color(0xFF2D322D)
private val inverseOnSurfaceLightMediumContrast = Color(0xFFEEF2EA)
private val inversePrimaryLightMediumContrast = Color(0xFF97D5A5)
private val surfaceDimLightMediumContrast = Color(0xFFD7DBD4)
private val surfaceBrightLightMediumContrast = Color(0xFFF6FBF3)
private val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightMediumContrast = Color(0xFFF0F5ED)
private val surfaceContainerLightMediumContrast = Color(0xFFEBEFE7)
private val surfaceContainerHighLightMediumContrast = Color(0xFFE5EAE2)
private val surfaceContainerHighestLightMediumContrast = Color(0xFFDFE4DC)

private val primaryLightHighContrast = Color(0xFF002911)
private val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
private val primaryContainerLightHighContrast = Color(0xFF0F4D29)
private val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val secondaryLightHighContrast = Color(0xFF142619)
private val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
private val secondaryContainerLightHighContrast = Color(0xFF344738)
private val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryLightHighContrast = Color(0xFF00262E)
private val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
private val tertiaryContainerLightHighContrast = Color(0xFF1C4952)
private val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
private val errorLightHighContrast = Color(0xFF4E0002)
private val onErrorLightHighContrast = Color(0xFFFFFFFF)
private val errorContainerLightHighContrast = Color(0xFF8C0009)
private val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
private val backgroundLightHighContrast = Color(0xFFF6FBF3)
private val onBackgroundLightHighContrast = Color(0xFF181D18)
private val surfaceLightHighContrast = Color(0xFFF6FBF3)
private val onSurfaceLightHighContrast = Color(0xFF000000)
private val surfaceVariantLightHighContrast = Color(0xFFDDE5DA)
private val onSurfaceVariantLightHighContrast = Color(0xFF1E261F)
private val outlineLightHighContrast = Color(0xFF3D453D)
private val outlineVariantLightHighContrast = Color(0xFF3D453D)
private val scrimLightHighContrast = Color(0xFF000000)
private val inverseSurfaceLightHighContrast = Color(0xFF2D322D)
private val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
private val inversePrimaryLightHighContrast = Color(0xFFBCFBC8)
private val surfaceDimLightHighContrast = Color(0xFFD7DBD4)
private val surfaceBrightLightHighContrast = Color(0xFFF6FBF3)
private val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
private val surfaceContainerLowLightHighContrast = Color(0xFFF0F5ED)
private val surfaceContainerLightHighContrast = Color(0xFFEBEFE7)
private val surfaceContainerHighLightHighContrast = Color(0xFFE5EAE2)
private val surfaceContainerHighestLightHighContrast = Color(0xFFDFE4DC)


private val primaryDark = Color(0xFF97D5A5)
private val onPrimaryDark = Color(0xFF00391A)
private val primaryContainerDark = Color(0xFF15512D)
private val onPrimaryContainerDark = Color(0xFFB3F1BF)
private val secondaryDark = Color(0xFFB6CCB8)
private val onSecondaryDark = Color(0xFF223526)
private val secondaryContainerDark = Color(0xFF384B3C)
private val onSecondaryContainerDark = Color(0xFFD2E8D3)
private val tertiaryDark = Color(0xFFA2CED9)
private val onTertiaryDark = Color(0xFF01363F)
private val tertiaryContainerDark = Color(0xFF204D56)
private val onTertiaryContainerDark = Color(0xFFBEEAF6)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF101510)
private val onBackgroundDark = Color(0xFFDFE4DC)
private val surfaceDark = Color(0xFF101510)
private val onSurfaceDark = Color(0xFFDFE4DC)
private val surfaceVariantDark = Color(0xFF414941)
private val onSurfaceVariantDark = Color(0xFFC1C9BF)
private val outlineDark = Color(0xFF8B938A)
private val outlineVariantDark = Color(0xFF414941)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFDFE4DC)
private val inverseOnSurfaceDark = Color(0xFF2D322D)
private val inversePrimaryDark = Color(0xFF306A42)
private val surfaceDimDark = Color(0xFF101510)
private val surfaceBrightDark = Color(0xFF353A35)
private val surfaceContainerLowestDark = Color(0xFF0A0F0B)
private val surfaceContainerLowDark = Color(0xFF181D18)
private val surfaceContainerDark = Color(0xFF1C211C)
private val surfaceContainerHighDark = Color(0xFF262B26)
private val surfaceContainerHighestDark = Color(0xFF313631)

private val primaryDarkMediumContrast = Color(0xFF9BD9A9)
private val onPrimaryDarkMediumContrast = Color(0xFF001B09)
private val primaryContainerDarkMediumContrast = Color(0xFF639E72)
private val onPrimaryContainerDarkMediumContrast = Color(0xFF000000)
private val secondaryDarkMediumContrast = Color(0xFFBBD0BC)
private val onSecondaryDarkMediumContrast = Color(0xFF081A0D)
private val secondaryContainerDarkMediumContrast = Color(0xFF819683)
private val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
private val tertiaryDarkMediumContrast = Color(0xFFA6D2DD)
private val onTertiaryDarkMediumContrast = Color(0xFF00191F)
private val tertiaryContainerDarkMediumContrast = Color(0xFF6D97A2)
private val onTertiaryContainerDarkMediumContrast = Color(0xFF000000)
private val errorDarkMediumContrast = Color(0xFFFFBAB1)
private val onErrorDarkMediumContrast = Color(0xFF370001)
private val errorContainerDarkMediumContrast = Color(0xFFFF5449)
private val onErrorContainerDarkMediumContrast = Color(0xFF000000)
private val backgroundDarkMediumContrast = Color(0xFF101510)
private val onBackgroundDarkMediumContrast = Color(0xFFDFE4DC)
private val surfaceDarkMediumContrast = Color(0xFF101510)
private val onSurfaceDarkMediumContrast = Color(0xFFF8FCF4)
private val surfaceVariantDarkMediumContrast = Color(0xFF414941)
private val onSurfaceVariantDarkMediumContrast = Color(0xFFC5CDC3)
private val outlineDarkMediumContrast = Color(0xFF9DA59C)
private val outlineVariantDarkMediumContrast = Color(0xFF7D857D)
private val scrimDarkMediumContrast = Color(0xFF000000)
private val inverseSurfaceDarkMediumContrast = Color(0xFFDFE4DC)
private val inverseOnSurfaceDarkMediumContrast = Color(0xFF262B26)
private val inversePrimaryDarkMediumContrast = Color(0xFF16522E)
private val surfaceDimDarkMediumContrast = Color(0xFF101510)
private val surfaceBrightDarkMediumContrast = Color(0xFF353A35)
private val surfaceContainerLowestDarkMediumContrast = Color(0xFF0A0F0B)
private val surfaceContainerLowDarkMediumContrast = Color(0xFF181D18)
private val surfaceContainerDarkMediumContrast = Color(0xFF1C211C)
private val surfaceContainerHighDarkMediumContrast = Color(0xFF262B26)
private val surfaceContainerHighestDarkMediumContrast = Color(0xFF313631)

private val primaryDarkHighContrast = Color(0xFFEFFFEE)
private val onPrimaryDarkHighContrast = Color(0xFF000000)
private val primaryContainerDarkHighContrast = Color(0xFF9BD9A9)
private val onPrimaryContainerDarkHighContrast = Color(0xFF000000)
private val secondaryDarkHighContrast = Color(0xFFEFFFEE)
private val onSecondaryDarkHighContrast = Color(0xFF000000)
private val secondaryContainerDarkHighContrast = Color(0xFFBBD0BC)
private val onSecondaryContainerDarkHighContrast = Color(0xFF000000)
private val tertiaryDarkHighContrast = Color(0xFFF3FCFF)
private val onTertiaryDarkHighContrast = Color(0xFF000000)
private val tertiaryContainerDarkHighContrast = Color(0xFFA6D2DD)
private val onTertiaryContainerDarkHighContrast = Color(0xFF000000)
private val errorDarkHighContrast = Color(0xFFFFF9F9)
private val onErrorDarkHighContrast = Color(0xFF000000)
private val errorContainerDarkHighContrast = Color(0xFFFFBAB1)
private val onErrorContainerDarkHighContrast = Color(0xFF000000)
private val backgroundDarkHighContrast = Color(0xFF101510)
private val onBackgroundDarkHighContrast = Color(0xFFDFE4DC)
private val surfaceDarkHighContrast = Color(0xFF101510)
private val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
private val surfaceVariantDarkHighContrast = Color(0xFF414941)
private val onSurfaceVariantDarkHighContrast = Color(0xFFF5FDF2)
private val outlineDarkHighContrast = Color(0xFFC5CDC3)
private val outlineVariantDarkHighContrast = Color(0xFFC5CDC3)
private val scrimDarkHighContrast = Color(0xFF000000)
private val inverseSurfaceDarkHighContrast = Color(0xFFDFE4DC)
private val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
private val inversePrimaryDarkHighContrast = Color(0xFF003216)
private val surfaceDimDarkHighContrast = Color(0xFF101510)
private val surfaceBrightDarkHighContrast = Color(0xFF353A35)
private val surfaceContainerLowestDarkHighContrast = Color(0xFF0A0F0B)
private val surfaceContainerLowDarkHighContrast = Color(0xFF181D18)
private val surfaceContainerDarkHighContrast = Color(0xFF1C211C)
private val surfaceContainerHighDarkHighContrast = Color(0xFF262B26)
private val surfaceContainerHighestDarkHighContrast = Color(0xFF313631)


@Composable
fun greenTheme(isDark: Boolean, themeContrast: ThemeContrast): ColorScheme {
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