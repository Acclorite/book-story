package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Double Click Translation setting.
 * Changes Reader's double click translation.
 */
@Composable
fun DoubleClickTranslationSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SwitchWithTitle(
        selected = state.value.doubleClickTranslation,
        title = stringResource(id = R.string.double_click_translation_option),
        description = stringResource(id = R.string.double_click_translation_option_desc),
        onClick = {
            onMainEvent(
                MainEvent.OnChangeDoubleClickTranslation(
                    !state.value.doubleClickTranslation
                )
            )
        }
    )
}