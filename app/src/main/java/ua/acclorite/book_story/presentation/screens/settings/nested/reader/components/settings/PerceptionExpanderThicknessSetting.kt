package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle

/**
 * Perception Expander Thickness setting.
 * Changes thickness of the line.
 */
@Composable
fun PerceptionExpanderThicknessSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SliderWithTitle(
        value = state.value.perceptionExpanderThickness to "pt",
        fromValue = 1,
        toValue = 12,
        title = stringResource(id = R.string.perception_expander_thickness_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangePerceptionExpanderThickness(it)
            )
        }
    )
}