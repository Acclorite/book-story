package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Perception Expander setting.
 * If true, shows vertical lines in Reader, which makes you read faster.
 */
@Composable
fun PerceptionExpanderSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SwitchWithTitle(
        selected = state.value.perceptionExpander,
        title = stringResource(id = R.string.perception_expander_option),
        description = stringResource(id = R.string.perception_expander_option_desc),
        onClick = {
            onMainEvent(
                MainEvent.OnChangePerceptionExpander(
                    !state.value.perceptionExpander
                )
            )
        }
    )
}