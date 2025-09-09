/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.crash

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.view.WindowCompat
import ua.blindmint.codex.domain.ui.ThemeContrast
import ua.blindmint.codex.ui.theme.BookStoryTheme
import ua.blindmint.codex.ui.theme.Theme

class CrashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Default super
        super.onCreate(savedInstanceState)

        // Edge to edge
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BookStoryTheme(
                theme = Theme.DYNAMIC,
                isDark = isSystemInDarkTheme(),
                isPureDark = false,
                themeContrast = ThemeContrast.STANDARD
            ) {
                CrashScreen.Content()
            }
        }
    }
}