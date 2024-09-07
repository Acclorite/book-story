package ua.acclorite.book_story.presentation.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel

@Composable
fun blackTheme(initialTheme: ColorScheme): ColorScheme {
    val absoluteDark = LocalMainViewModel.current.state.value.absoluteDark
    val surfaceDarker = 3f
    val surfaceContainerDarker = if (absoluteDark) 3f else 1.95f

    return initialTheme.copy(
        surface = initialTheme.surface.run {
            if (absoluteDark) {
                return@run Color.Black
            }

            darkenBy(surfaceDarker)
        },
        surfaceContainer = initialTheme.surfaceContainer.darkenBy(
            surfaceContainerDarker
        ),
        surfaceContainerLowest = initialTheme.surfaceContainerLowest.darkenBy(
            surfaceContainerDarker
        ),
        surfaceContainerLow = initialTheme.surfaceContainerLow.darkenBy(
            surfaceContainerDarker
        ),
        surfaceContainerHigh = initialTheme.surfaceContainerHigh.darkenBy(
            surfaceContainerDarker
        ),
        surfaceContainerHighest = initialTheme.surfaceContainerHighest.darkenBy(
            surfaceContainerDarker
        )
    )
}

private fun Color.darkenBy(value: Float): Color {
    return copy(
        red = red / value,
        green = green / value,
        blue = blue / value
    )
}