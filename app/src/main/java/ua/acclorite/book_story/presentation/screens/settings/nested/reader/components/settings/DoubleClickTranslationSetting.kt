package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Double Click Translation setting.
 * Changes Reader's double click translation.
 */
@Composable
fun LazyItemScope.DoubleClickTranslationSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SwitchWithTitle(
        selected = state.value.doubleClickTranslation!!,
        modifier = Modifier.animateItem(),
        title = stringResource(id = R.string.translator_double_click_to_translate_option),
        description = stringResource(id = R.string.translator_double_click_to_translate_option_desc),
        onClick = {
            onMainEvent(
                MainEvent.OnChangeDoubleClickTranslation(
                    !state.value.doubleClickTranslation!!
                )
            )
        }
    )
}