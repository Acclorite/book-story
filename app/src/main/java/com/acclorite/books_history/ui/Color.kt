package com.acclorite.books_history.ui

import android.annotation.SuppressLint
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.acclorite.books_history.ui.theme.blueTheme
import com.acclorite.books_history.ui.theme.greenTheme
import com.acclorite.books_history.ui.theme.pinkTheme
import com.acclorite.books_history.ui.theme.purpleTheme

enum class Theme {
    DYNAMIC,
    BLUE,
    PURPLE,
    GREEN,
    PINK
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

        else -> {
            /* Pink Theme */
            return pinkTheme(isDark = darkTheme)
        }
    }
}