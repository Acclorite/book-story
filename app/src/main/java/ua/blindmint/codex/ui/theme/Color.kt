/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.theme

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.ui.ThemeContrast
import ua.blindmint.codex.ui.theme.color.aquaTheme
import ua.blindmint.codex.ui.theme.color.blackTheme
import ua.blindmint.codex.ui.theme.color.blueTheme
import ua.blindmint.codex.ui.theme.color.dynamicTheme
import ua.blindmint.codex.ui.theme.color.green2Theme
import ua.blindmint.codex.ui.theme.color.greenGrayTheme
import ua.blindmint.codex.ui.theme.color.greenTheme
import ua.blindmint.codex.ui.theme.color.lavenderTheme
import ua.blindmint.codex.ui.theme.color.marshTheme
import ua.blindmint.codex.ui.theme.color.pink2Theme
import ua.blindmint.codex.ui.theme.color.pinkTheme
import ua.blindmint.codex.ui.theme.color.purpleGrayTheme
import ua.blindmint.codex.ui.theme.color.purpleTheme
import ua.blindmint.codex.ui.theme.color.redGrayTheme
import ua.blindmint.codex.ui.theme.color.redTheme
import ua.blindmint.codex.ui.theme.color.yellow2Theme
import ua.blindmint.codex.ui.theme.color.yellowTheme


@Immutable
enum class Theme(
    val hasThemeContrast: Boolean,
    @StringRes val title: Int
) {
    DYNAMIC(hasThemeContrast = false, title = R.string.dynamic_theme),
    BLUE(hasThemeContrast = true, title = R.string.blue_theme),
    GREEN(hasThemeContrast = true, title = R.string.green_theme),
    GREEN2(hasThemeContrast = false, title = R.string.green2_theme),
    GREEN_GRAY(hasThemeContrast = false, title = R.string.green_gray_theme),
    MARSH(hasThemeContrast = true, title = R.string.marsh_theme),
    RED(hasThemeContrast = true, title = R.string.red_theme),
    RED_GRAY(hasThemeContrast = false, title = R.string.red_gray_theme),
    PURPLE(hasThemeContrast = true, title = R.string.purple_theme),
    PURPLE_GRAY(hasThemeContrast = false, title = R.string.purple_gray_theme),
    LAVENDER(hasThemeContrast = true, title = R.string.lavender_theme),
    PINK(hasThemeContrast = true, title = R.string.pink_theme),
    PINK2(hasThemeContrast = false, title = R.string.pink2_theme),
    YELLOW(hasThemeContrast = true, title = R.string.yellow_theme),
    YELLOW2(hasThemeContrast = false, title = R.string.yellow2_theme),
    AQUA(hasThemeContrast = true, title = R.string.aqua_theme);

    companion object {
        fun entries(): List<Theme> {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> Theme.entries
                else -> Theme.entries.dropWhile { it == DYNAMIC }
            }
        }
    }
}

/**
 * Converting [String] into [Theme].
 */
fun String.toTheme(): Theme {
    return Theme.valueOf(this)
}

/**
 * Creates a colorscheme based on [Theme].
 *
 * @param theme a [Theme].
 *
 * @return a [ColorScheme].
 */
@Composable
fun colorScheme(
    theme: Theme,
    darkTheme: Boolean,
    isPureDark: Boolean,
    themeContrast: ThemeContrast
): ColorScheme {
    val colorScheme = when (theme) {
        Theme.DYNAMIC -> {
            /* Dynamic Theme */
            dynamicTheme(isDark = darkTheme)
        }

        Theme.BLUE -> {
            /* Blue Theme */
            blueTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.PURPLE -> {
            /* Purple Theme */
            purpleTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.PURPLE_GRAY -> {
            /* Purple Gray Theme */
            purpleGrayTheme(isDark = darkTheme)
        }

        Theme.GREEN -> {
            /* Green Theme */
            greenTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.GREEN2 -> {
            /* Green2 Theme */
            green2Theme(isDark = darkTheme)
        }

        Theme.GREEN_GRAY -> {
            /* Green Gray Theme */
            greenGrayTheme(isDark = darkTheme)
        }

        Theme.MARSH -> {
            /* Marsh Theme */
            marshTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.PINK -> {
            /* Pink Theme */
            pinkTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.PINK2 -> {
            /* Pink2 Theme */
            pink2Theme(isDark = darkTheme)
        }

        Theme.LAVENDER -> {
            /* Lavender Theme */
            lavenderTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.YELLOW -> {
            /* Yellow Theme */
            yellowTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.YELLOW2 -> {
            /* Yellow2 Theme */
            yellow2Theme(isDark = darkTheme)
        }

        Theme.RED -> {
            /* Red Theme */
            redTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.RED_GRAY -> {
            /* Red Gray Theme */
            redGrayTheme(isDark = darkTheme)
        }

        Theme.AQUA -> {
            /* Aqua Theme */
            aquaTheme(isDark = darkTheme, themeContrast = themeContrast)
        }
    }

    return if (isPureDark && darkTheme) {
        blackTheme(initialTheme = colorScheme)
    } else {
        colorScheme
    }
}