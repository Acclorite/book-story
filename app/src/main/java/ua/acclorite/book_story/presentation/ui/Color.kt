package ua.acclorite.book_story.presentation.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.presentation.ui.theme.aquaTheme
import ua.acclorite.book_story.presentation.ui.theme.blackTheme
import ua.acclorite.book_story.presentation.ui.theme.blueTheme
import ua.acclorite.book_story.presentation.ui.theme.dynamicTheme
import ua.acclorite.book_story.presentation.ui.theme.greenTheme
import ua.acclorite.book_story.presentation.ui.theme.lavenderTheme
import ua.acclorite.book_story.presentation.ui.theme.marshTheme
import ua.acclorite.book_story.presentation.ui.theme.pinkTheme
import ua.acclorite.book_story.presentation.ui.theme.purpleTheme
import ua.acclorite.book_story.presentation.ui.theme.redTheme
import ua.acclorite.book_story.presentation.ui.theme.yellowTheme

@Immutable
enum class Theme {
    DYNAMIC,
    BLUE,
    GREEN,
    RED,
    PURPLE,
    PINK,
    YELLOW,
    AQUA,
    MARSH,
    LAVENDER,
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

        Theme.GREEN -> {
            /* Green Theme */
            greenTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.MARSH -> {
            /* Marsh Theme */
            marshTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.PINK -> {
            /* Pink Theme */
            pinkTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.LAVENDER -> {
            /* Lavender Theme */
            lavenderTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.YELLOW -> {
            /* Yellow Theme */
            yellowTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.RED -> {
            /* Red Theme */
            redTheme(isDark = darkTheme, themeContrast = themeContrast)
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