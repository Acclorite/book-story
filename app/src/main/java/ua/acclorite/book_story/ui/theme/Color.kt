/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.ui.theme.color.aquaTheme
import ua.acclorite.book_story.ui.theme.color.blackTheme
import ua.acclorite.book_story.ui.theme.color.blueTheme
import ua.acclorite.book_story.ui.theme.color.dynamicTheme
import ua.acclorite.book_story.ui.theme.color.grayTheme
import ua.acclorite.book_story.ui.theme.color.green2Theme
import ua.acclorite.book_story.ui.theme.color.greenGrayTheme
import ua.acclorite.book_story.ui.theme.color.greenTheme
import ua.acclorite.book_story.ui.theme.color.lavenderTheme
import ua.acclorite.book_story.ui.theme.color.marshTheme
import ua.acclorite.book_story.ui.theme.color.pink2Theme
import ua.acclorite.book_story.ui.theme.color.pinkTheme
import ua.acclorite.book_story.ui.theme.color.purpleGrayTheme
import ua.acclorite.book_story.ui.theme.color.purpleTheme
import ua.acclorite.book_story.ui.theme.color.redGrayTheme
import ua.acclorite.book_story.ui.theme.color.redTheme
import ua.acclorite.book_story.ui.theme.color.yellow2Theme
import ua.acclorite.book_story.ui.theme.color.yellowTheme
import ua.acclorite.book_story.ui.theme.model.Theme
import ua.acclorite.book_story.ui.theme.model.ThemeContrast


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

        Theme.GRAY -> {
            /* Gray Theme */
            grayTheme(isDark = darkTheme)
        }
    }

    return if (isPureDark && darkTheme) {
        blackTheme(initialTheme = colorScheme)
    } else {
        colorScheme
    }
}