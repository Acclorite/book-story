package ua.acclorite.book_story.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalContext
import ua.acclorite.book_story.presentation.ui.theme.aquaTheme
import ua.acclorite.book_story.presentation.ui.theme.blueTheme
import ua.acclorite.book_story.presentation.ui.theme.greenTheme
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
    AQUA
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
@SuppressLint("NewApi")
@Composable
fun colorScheme(theme: Theme, darkTheme: Boolean, themeContrast: ThemeContrast): ColorScheme {
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
            return blueTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.PURPLE -> {
            /* Purple Theme */
            return purpleTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.GREEN -> {
            /* Green Theme */
            return greenTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.PINK -> {
            /* Pink Theme */
            return pinkTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.YELLOW -> {
            /* Yellow Theme */
            return yellowTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.RED -> {
            /* Red Theme */
            return redTheme(isDark = darkTheme, themeContrast = themeContrast)
        }

        Theme.AQUA -> {
            /* Aqua Theme */
            return aquaTheme(isDark = darkTheme, themeContrast = themeContrast)
        }
    }
}