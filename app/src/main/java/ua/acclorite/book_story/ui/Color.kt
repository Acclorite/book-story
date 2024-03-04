package ua.acclorite.book_story.ui

import android.annotation.SuppressLint
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ua.acclorite.book_story.ui.theme.aquaTheme
import ua.acclorite.book_story.ui.theme.blueTheme
import ua.acclorite.book_story.ui.theme.greenTheme
import ua.acclorite.book_story.ui.theme.pinkTheme
import ua.acclorite.book_story.ui.theme.purpleTheme
import ua.acclorite.book_story.ui.theme.redTheme
import ua.acclorite.book_story.ui.theme.yellowTheme

enum class Theme {
    DYNAMIC,
    BLUE,
    GREEN,
    RED,
    PURPLE,
    PINK,
    YELLOW,
    AQUA
}

/**
 * Converting [String] into [Theme].
 */
fun String.toTheme(): Theme {
    return when (this) {
        "DYNAMIC" -> Theme.DYNAMIC
        "PURPLE" -> Theme.PURPLE
        "GREEN" -> Theme.GREEN
        "PINK" -> Theme.PINK
        "YELLOW" -> Theme.YELLOW
        "RED" -> Theme.RED
        "AQUA" -> Theme.AQUA
        else -> Theme.BLUE
    }
}

/**
 * Creates a colorscheme based on [Theme].
 *
 * @param theme a [Theme].
 *
 * @return a [ColorScheme].
 */
@SuppressLint("NewApi")
@Composable
fun colorScheme(theme: Theme, darkTheme: Boolean): ColorScheme {
    when (theme) {
        Theme.DYNAMIC -> {
            /* Dynamic Theme */
            return if (darkTheme)
                dynamicDarkColorScheme(LocalContext.current)
            else
                dynamicLightColorScheme(LocalContext.current)
        }

        Theme.BLUE -> {
            /* Blue Theme */
            return blueTheme(isDark = darkTheme)
        }

        Theme.PURPLE -> {
            /* Purple Theme */
            return purpleTheme(isDark = darkTheme)
        }

        Theme.GREEN -> {
            /* Green Theme */
            return greenTheme(isDark = darkTheme)
        }

        Theme.PINK -> {
            /* Pink Theme */
            return pinkTheme(isDark = darkTheme)
        }

        Theme.YELLOW -> {
            /* Yellow Theme */
            return yellowTheme(isDark = darkTheme)
        }

        Theme.RED -> {
            /* Red Theme */
            return redTheme(isDark = darkTheme)
        }

        Theme.AQUA -> {
            /* Aqua Theme */
            return aquaTheme(isDark = darkTheme)
        }
    }
}