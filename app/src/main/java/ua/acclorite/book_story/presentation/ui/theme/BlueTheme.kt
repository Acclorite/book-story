package ua.acclorite.book_story.presentation.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val md_theme_light_primary = Color(0xFF005DB8)
private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
private val md_theme_light_primaryContainer = Color(0xFFD6E3FF)
private val md_theme_light_onPrimaryContainer = Color(0xFF001B3E)
private val md_theme_light_secondary = Color(0xFF565F71)
private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
private val md_theme_light_secondaryContainer = Color(0xFFDAE2F9)
private val md_theme_light_onSecondaryContainer = Color(0xFF131C2B)
private val md_theme_light_tertiary = Color(0xFF6F5575)
private val md_theme_light_onTertiary = Color(0xFFFFFFFF)
private val md_theme_light_tertiaryContainer = Color(0xFFF9D8FD)
private val md_theme_light_onTertiaryContainer = Color(0xFF28132E)
private val md_theme_light_error = Color(0xFFBA1A1A)
private val md_theme_light_errorContainer = Color(0xFFFFDAD6)
private val md_theme_light_onError = Color(0xFFFFFFFF)
private val md_theme_light_onErrorContainer = Color(0xFF410002)
private val md_theme_light_background = Color(0xFFFDFBFF)
private val md_theme_light_onBackground = Color(0xFF1A1B1E)
private val md_theme_light_surface = Color(0xFFFDFBFF)
private val md_theme_light_onSurface = Color(0xFF1A1B1E)
private val md_theme_light_surfaceVariant = Color(0xFFE0E2EC)
private val md_theme_light_onSurfaceVariant = Color(0xFF44474E)
private val md_theme_light_outline = Color(0xFF74777F)
private val md_theme_light_inverseOnSurface = Color(0xFFF1F0F4)
private val md_theme_light_inverseSurface = Color(0xFF2F3033)
private val md_theme_light_inversePrimary = Color(0xFFAAC7FF)
private val md_theme_light_surfaceTint = Color(0xFF005DB8)
private val md_theme_light_outlineVariant = Color(0xFFC4C6D0)
private val md_theme_light_scrim = Color(0xFF000000)

private val md_theme_dark_primary = Color(0xFFAAC7FF)
private val md_theme_dark_onPrimary = Color(0xFF002F64)
private val md_theme_dark_primaryContainer = Color(0xFF00458D)
private val md_theme_dark_onPrimaryContainer = Color(0xFFD6E3FF)
private val md_theme_dark_secondary = Color(0xFFBEC6DC)
private val md_theme_dark_onSecondary = Color(0xFF283141)
private val md_theme_dark_secondaryContainer = Color(0xFF3E4759)
private val md_theme_dark_onSecondaryContainer = Color(0xFFDAE2F9)
private val md_theme_dark_tertiary = Color(0xFFDCBCE0)
private val md_theme_dark_onTertiary = Color(0xFF3F2844)
private val md_theme_dark_tertiaryContainer = Color(0xFF573E5C)
private val md_theme_dark_onTertiaryContainer = Color(0xFFF9D8FD)
private val md_theme_dark_error = Color(0xFFFFB4AB)
private val md_theme_dark_errorContainer = Color(0xFF93000A)
private val md_theme_dark_onError = Color(0xFF690005)
private val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
private val md_theme_dark_background = Color(0xFF1A1B1E)
private val md_theme_dark_onBackground = Color(0xFFE3E2E6)
private val md_theme_dark_surface = Color(0xFF1A1B1E)
private val md_theme_dark_onSurface = Color(0xFFE3E2E6)
private val md_theme_dark_surfaceVariant = Color(0xFF44474E)
private val md_theme_dark_onSurfaceVariant = Color(0xFFC4C6D0)
private val md_theme_dark_outline = Color(0xFF8E9099)
private val md_theme_dark_inverseOnSurface = Color(0xFF1A1B1E)
private val md_theme_dark_inverseSurface = Color(0xFFE3E2E6)
private val md_theme_dark_inversePrimary = Color(0xFF005DB8)
private val md_theme_dark_surfaceTint = Color(0xFFAAC7FF)
private val md_theme_dark_outlineVariant = Color(0xFF44474E)
private val md_theme_dark_scrim = Color(0xFF000000)


@Composable
fun blueTheme(isDark: Boolean): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = md_theme_dark_primary,
            onPrimary = md_theme_dark_onPrimary,
            primaryContainer = md_theme_dark_primaryContainer,
            onPrimaryContainer = md_theme_dark_onPrimaryContainer,
            secondary = md_theme_dark_secondary,
            onSecondary = md_theme_dark_onSecondary,
            secondaryContainer = md_theme_dark_secondaryContainer,
            onSecondaryContainer = md_theme_dark_onSecondaryContainer,
            tertiary = md_theme_dark_tertiary,
            onTertiary = md_theme_dark_onTertiary,
            tertiaryContainer = md_theme_dark_tertiaryContainer,
            onTertiaryContainer = md_theme_dark_onTertiaryContainer,
            error = md_theme_dark_error,
            errorContainer = md_theme_dark_errorContainer,
            onError = md_theme_dark_onError,
            onErrorContainer = md_theme_dark_onErrorContainer,
            background = md_theme_dark_background,
            onBackground = md_theme_dark_onBackground,
            surface = md_theme_dark_surface,
            onSurface = md_theme_dark_onSurface,
            surfaceVariant = md_theme_dark_surfaceVariant,
            onSurfaceVariant = md_theme_dark_onSurfaceVariant,
            outline = md_theme_dark_outline,
            inverseOnSurface = md_theme_dark_inverseOnSurface,
            inverseSurface = md_theme_dark_inverseSurface,
            inversePrimary = md_theme_dark_inversePrimary,
            surfaceTint = md_theme_dark_surfaceTint,
            outlineVariant = md_theme_dark_outlineVariant,
            scrim = md_theme_dark_scrim
        )
    } else {
        lightColorScheme(
            primary = md_theme_light_primary,
            onPrimary = md_theme_light_onPrimary,
            primaryContainer = md_theme_light_primaryContainer,
            onPrimaryContainer = md_theme_light_onPrimaryContainer,
            secondary = md_theme_light_secondary,
            onSecondary = md_theme_light_onSecondary,
            secondaryContainer = md_theme_light_secondaryContainer,
            onSecondaryContainer = md_theme_light_onSecondaryContainer,
            tertiary = md_theme_light_tertiary,
            onTertiary = md_theme_light_onTertiary,
            tertiaryContainer = md_theme_light_tertiaryContainer,
            onTertiaryContainer = md_theme_light_onTertiaryContainer,
            error = md_theme_light_error,
            errorContainer = md_theme_light_errorContainer,
            onError = md_theme_light_onError,
            onErrorContainer = md_theme_light_onErrorContainer,
            background = md_theme_light_background,
            onBackground = md_theme_light_onBackground,
            surface = md_theme_light_surface,
            onSurface = md_theme_light_onSurface,
            surfaceVariant = md_theme_light_surfaceVariant,
            onSurfaceVariant = md_theme_light_onSurfaceVariant,
            outline = md_theme_light_outline,
            inverseOnSurface = md_theme_light_inverseOnSurface,
            inverseSurface = md_theme_light_inverseSurface,
            inversePrimary = md_theme_light_inversePrimary,
            surfaceTint = md_theme_light_surfaceTint,
            outlineVariant = md_theme_light_outlineVariant,
            scrim = md_theme_light_scrim
        )
    }
}