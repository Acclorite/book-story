package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.settings.SliderWithTitle
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel

/**
 * Perception Expander Padding setting.
 * Changes side padding applied to lines.
 */
@Composable
fun PerceptionExpanderPaddingSetting() {
    val state = MainViewModel.getState()
    val onMainEvent = MainViewModel.getEvent()

    SliderWithTitle(
        value = state.value.perceptionExpanderPadding to "pt",
        fromValue = 0,
        toValue = 24,
        title = stringResource(id = R.string.perception_expander_padding_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangePerceptionExpanderPadding(it)
            )
        }
    )
}