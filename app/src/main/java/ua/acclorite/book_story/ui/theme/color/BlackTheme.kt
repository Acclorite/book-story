package ua.acclorite.book_story.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun blackTheme(initialTheme: ColorScheme): ColorScheme {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    val surfaceDarker = 3f
    val surfaceContainerDarker = if (state.value.absoluteDark) 3f else 1.95f

    return initialTheme.copy(
        surface = initialTheme.surface.run {
            if (state.value.absoluteDark) {
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