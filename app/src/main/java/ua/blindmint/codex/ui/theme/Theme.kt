/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ua.blindmint.codex.domain.ui.ThemeContrast

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