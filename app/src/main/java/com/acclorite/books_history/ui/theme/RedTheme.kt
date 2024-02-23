package com.acclorite.books_history.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val md_theme_light_primary = Color(0xFF9C4143)
private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
private val md_theme_light_primaryContainer = Color(0xFFFFDAD8)
private val md_theme_light_onPrimaryContainer = Color(0xFF410007)
private val md_theme_light_secondary = Color(0xFF775655)
private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
private val md_theme_light_secondaryContainer = Color(0xFFFFDAD8)
private val md_theme_light_onSecondaryContainer = Color(0xFF2C1515)
private val md_theme_light_tertiary = Color(0xFF745A2F)
private val md_theme_light_onTertiary = Color(0xFFFFFFFF)
private val md_theme_light_tertiaryContainer = Color(0xFFFFDEAC)
private val md_theme_light_onTertiaryContainer = Color(0xFF281900)
private val md_theme_light_error = Color(0xFFBA1A1A)
private val md_theme_light_errorContainer = Color(0xFFFFDAD6)
private val md_theme_light_onError = Color(0xFFFFFFFF)
private val md_theme_light_onErrorContainer = Color(0xFF410002)
private val md_theme_light_background = Color(0xFFFFFBFF)
private val md_theme_light_onBackground = Color(0xFF201A1A)
private val md_theme_light_surface = Color(0xFFFFFBFF)
private val md_theme_light_onSurface = Color(0xFF201A1A)
private val md_theme_light_surfaceVariant = Color(0xFFF4DDDC)
private val md_theme_light_onSurfaceVariant = Color(0xFF524343)
private val md_theme_light_outline = Color(0xFF857372)
private val md_theme_light_inverseOnSurface = Color(0xFFFBEEED)
private val md_theme_light_inverseSurface = Color(0xFF362F2E)
private val md_theme_light_inversePrimary = Color(0xFFFFB3B1)
private val md_theme_light_surfaceTint = Color(0xFF9C4143)
private val md_theme_light_outlineVariant = Color(0xFFD7C1C0)
private val md_theme_light_scrim = Color(0xFF000000)

private val md_theme_dark_primary = Color(0xFFFFB3B1)
private val md_theme_dark_onPrimary = Color(0xFF5F1319)
private val md_theme_dark_primaryContainer = Color(0xFF7E2A2D)
private val md_theme_dark_onPrimaryContainer = Color(0xFFFFDAD8)
private val md_theme_dark_secondary = Color(0xFFE6BDBB)
private val md_theme_dark_onSecondary = Color(0xFF442929)
private val md_theme_dark_secondaryContainer = Color(0xFF5D3F3E)
private val md_theme_dark_onSecondaryContainer = Color(0xFFFFDAD8)
private val md_theme_dark_tertiary = Color(0xFFE4C18D)
private val md_theme_dark_onTertiary = Color(0xFF412D05)
private val md_theme_dark_tertiaryContainer = Color(0xFF5A4319)
private val md_theme_dark_onTertiaryContainer = Color(0xFFFFDEAC)
private val md_theme_dark_error = Color(0xFFFFB4AB)
private val md_theme_dark_errorContainer = Color(0xFF93000A)
private val md_theme_dark_onError = Color(0xFF690005)
private val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
private val md_theme_dark_background = Color(0xFF201A1A)
private val md_theme_dark_onBackground = Color(0xFFEDE0DF)
private val md_theme_dark_surface = Color(0xFF201A1A)
private val md_theme_dark_onSurface = Color(0xFFEDE0DF)
private val md_theme_dark_surfaceVariant = Color(0xFF524343)
private val md_theme_dark_onSurfaceVariant = Color(0xFFD7C1C0)
private val md_theme_dark_outline = Color(0xFFA08C8B)
private val md_theme_dark_inverseOnSurface = Color(0xFF201A1A)
private val md_theme_dark_inverseSurface = Color(0xFFEDE0DF)
private val md_theme_dark_inversePrimary = Color(0xFF9C4143)
private val md_theme_dark_surfaceTint = Color(0xFFFFB3B1)
private val md_theme_dark_outlineVariant = Color(0xFF524343)
private val md_theme_dark_scrim = Color(0xFF000000)

@Composable
fun redTheme(isDark: Boolean): ColorScheme {
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