package com.acclorite.books_history.ui

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowCompat

enum class DarkTheme {
    FOLLOW_SYSTEM,
    OFF,
    ON
}

fun String.toDarkTheme(): DarkTheme {
    return when (this) {
        "OFF" -> DarkTheme.OFF
        "ON" -> DarkTheme.ON
        else -> DarkTheme.FOLLOW_SYSTEM
    }
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
fun BooksHistoryResurrectionTheme(
    theme: Theme,
    isDark: Boolean,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            // Fix for nav bar being semi transparent in api 29+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme(
            theme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) theme
            else if (theme != Theme.DYNAMIC) theme else Theme.BLUE,
            darkTheme = isDark
        ),
        shapes = Shapes(),
        typography = Typography,
        content = content
    )
}

@Composable
fun MaterialTheme.elevation(elevation: Dp = NavigationBarDefaults.Elevation): Color =
    colorScheme.surfaceColorAtElevation(elevation)