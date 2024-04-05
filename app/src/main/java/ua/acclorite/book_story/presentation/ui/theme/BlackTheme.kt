package ua.acclorite.book_story.presentation.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun blackTheme(initialTheme: ColorScheme): ColorScheme {
    return initialTheme.copy(
        surface = initialTheme.surface.copy {
            val darker = 3f
            it.copy(
                red = it.red / darker,
                green = it.green / darker,
                blue = it.blue / darker
            )
        },
        surfaceContainer = initialTheme.surfaceContainer.copy {
            val darker = 1.95f
            it.copy(
                red = it.red / darker,
                green = it.green / darker,
                blue = it.blue / darker
            )
        },
        surfaceContainerLowest = initialTheme.surfaceContainerLowest.copy {
            val darker = 1.95f
            it.copy(
                red = it.red / darker,
                green = it.green / darker,
                blue = it.blue / darker
            )
        },
        surfaceContainerLow = initialTheme.surfaceContainerLow.copy {
            val darker = 1.95f
            it.copy(
                red = it.red / darker,
                green = it.green / darker,
                blue = it.blue / darker
            )
        },
        surfaceContainerHigh = initialTheme.surfaceContainerHigh.copy {
            val darker = 1.95f
            it.copy(
                red = it.red / darker,
                green = it.green / darker,
                blue = it.blue / darker
            )
        },
        surfaceContainerHighest = initialTheme.surfaceContainerHighest.copy {
            val darker = 1.95f
            it.copy(
                red = it.red / darker,
                green = it.green / darker,
                blue = it.blue / darker
            )
        },
    )
}

private fun Color.copy(calculation: (Color) -> Color): Color {
    return calculation(this)
}