package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle

/**
 * Perception Expander Padding setting.
 * Changes side padding applied to lines.
 */
@Composable
fun PerceptionExpanderPaddingSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

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