package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle

/**
 * Font Size setting.
 * Changes Reader's font size.
 */
@Composable
fun FontSizeSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SliderWithTitle(
        value = state.value.fontSize to "pt",
        fromValue = 10,
        toValue = 35,
        title = stringResource(id = R.string.font_size_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangeFontSize(it)
            )
        }
    )
}