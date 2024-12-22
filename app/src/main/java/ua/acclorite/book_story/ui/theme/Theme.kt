package ua.acclorite.book_story.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.os.PowerManager
import android.view.View
import androidx.annotation.Keep
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import kotlinx.parcelize.Parcelize

@Immutable
@Keep
@Parcelize
enum class PureDark : Parcelable {
    OFF,
    ON,
    SAVER
}

@Immutable
@Keep
@Parcelize
enum class ThemeContrast : Parcelable {
    STANDARD,
    MEDIUM,
    HIGH
}

@Immutable
@Keep
@Parcelize
enum class DarkTheme : Parcelable {
    FOLLOW_SYSTEM,
    OFF,
    ON
}

fun String.toDarkTheme(): DarkTheme {
    return DarkTheme.valueOf(this)
}

fun String.toPureDark(): PureDark {
    return PureDark.valueOf(this)
}

fun String.toThemeContrast(): ThemeContrast {
    return ThemeContrast.valueOf(this)
}

@Composable
fun DarkTheme.isDark(): Boolean {
    return when (this) {
        DarkTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkTheme.ON -> true
        DarkTheme.OFF -> false
    }
}

@Composable
fun PureDark.isPureDark(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    return when (this) {
        PureDark.OFF -> false
        PureDark.ON -> true
        PureDark.SAVER -> powerManager.isPowerSaveMode
    }
}

@Composable
fun BookStoryTheme(
    theme: Theme,
    isDark: Boolean,
    isPureDark: Boolean,
    themeContrast: ThemeContrast,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Disabling Autofill
            window.decorView.importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS

            // Fix for nav bar being semi transparent in api 29+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
        }
    }

    val colorScheme = colorScheme(
        theme = Theme.entries().find { it == theme } ?: Theme.entries().first(),
        darkTheme = isDark,
        isPureDark = isPureDark,
        themeContrast = themeContrast
    )
    val animatedColorScheme = animateColorScheme(targetColorScheme = colorScheme)

    MaterialTheme(
        colorScheme = animatedColorScheme,
        shapes = Shapes(),
        typography = Typography(),
        content = content
    )
}

object Colors {
    val readerSystemBarsColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.surfaceContainer.copy(0.9f)
}

@Composable
private fun animateColor(targetColor: Color): Color {
    return animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(300),
        label = ""
    ).value
}

@Composable
private fun animateColorScheme(targetColorScheme: ColorScheme): ColorScheme {
    return ColorScheme(
        primary = animateColor(targetColor = targetColorScheme.primary),
        onPrimary = animateColor(targetColor = targetColorScheme.onPrimary),
        primaryContainer = animateColor(targetColor = targetColorScheme.primaryContainer),
        onPrimaryContainer = animateColor(targetColor = targetColorScheme.onPrimaryContainer),
        secondary = animateColor(targetColor = targetColorScheme.secondary),
        onSecondary = animateColor(targetColor = targetColorScheme.onSecondary),
        secondaryContainer = animateColor(targetColor = targetColorScheme.secondaryContainer),
        onSecondaryContainer = animateColor(targetColor = targetColorScheme.onSecondaryContainer),
        tertiary = animateColor(targetColor = targetColorScheme.tertiary),
        onTertiary = animateColor(targetColor = targetColorScheme.onTertiary),
        tertiaryContainer = animateColor(targetColor = targetColorScheme.tertiaryContainer),
        onTertiaryContainer = animateColor(targetColor = targetColorScheme.onTertiaryContainer),
        error = animateColor(targetColor = targetColorScheme.error),
        errorContainer = animateColor(targetColor = targetColorScheme.errorContainer),
        onError = animateColor(targetColor = targetColorScheme.onError),
        onErrorContainer = animateColor(targetColor = targetColorScheme.onErrorContainer),
        background = animateColor(targetColor = targetColorScheme.background),
        onBackground = animateColor(targetColor = targetColorScheme.onBackground),
        surface = animateColor(targetColor = targetColorScheme.surface),
        onSurface = animateColor(targetColor = targetColorScheme.onSurface),
        surfaceVariant = animateColor(targetColor = targetColorScheme.surfaceVariant),
        onSurfaceVariant = animateColor(targetColor = targetColorScheme.onSurfaceVariant),
        outline = animateColor(targetColor = targetColorScheme.outline),
        inverseOnSurface = animateColor(targetColor = targetColorScheme.inverseOnSurface),
        inverseSurface = animateColor(targetColor = targetColorScheme.inverseSurface),
        inversePrimary = animateColor(targetColor = targetColorScheme.inversePrimary),
        surfaceTint = animateColor(targetColor = targetColorScheme.surfaceTint),
        outlineVariant = animateColor(targetColor = targetColorScheme.outlineVariant),
        scrim = animateColor(targetColor = targetColorScheme.scrim),
        surfaceBright = animateColor(targetColor = targetColorScheme.surfaceBright),
        surfaceDim = animateColor(targetColor = targetColorScheme.surfaceDim),
        surfaceContainer = animateColor(targetColor = targetColorScheme.surfaceContainer),
        surfaceContainerHigh = animateColor(targetColor = targetColorScheme.surfaceContainerHigh),
        surfaceContainerHighest = animateColor(targetColor = targetColorScheme.surfaceContainerHighest),
        surfaceContainerLow = animateColor(targetColor = targetColorScheme.surfaceContainerLow),
        surfaceContainerLowest = animateColor(targetColor = targetColorScheme.surfaceContainerLowest),
    )
}

@Composable
fun animatedColorScheme(
    theme: Theme,
    isDark: Boolean,
    isPureDark: Boolean,
    themeContrast: ThemeContrast
): ColorScheme {
    val colorScheme = colorScheme(
        theme = theme,
        darkTheme = isDark,
        isPureDark = isPureDark,
        themeContrast = themeContrast
    )

    return animateColorScheme(targetColorScheme = colorScheme)
}