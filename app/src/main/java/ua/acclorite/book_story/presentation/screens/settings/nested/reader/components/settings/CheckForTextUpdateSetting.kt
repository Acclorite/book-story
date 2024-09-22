package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Check for Text Update setting.
 * If true, automatically checks whether there is book's text update.
 */
@Composable
fun CheckForTextUpdateSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SwitchWithTitle(
        selected = state.value.checkForTextUpdate,
        title = stringResource(id = R.string.check_for_text_update_option),
        description = stringResource(id = R.string.check_for_text_update_option_desc),
        onClick = {
            onMainEvent(
                MainEvent.OnChangeCheckForTextUpdate(
                    !state.value.checkForTextUpdate
                )
            )
        }
    )
}