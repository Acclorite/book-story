package ua.acclorite.book_story.presentation.settings.reader.translator.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.settings.SwitchWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun DoubleClickTranslationOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SwitchWithTitle(
        selected = state.value.doubleClickTranslation,
        title = stringResource(id = R.string.double_click_translation_option),
        description = stringResource(id = R.string.double_click_translation_option_desc),
        onClick = {
            mainModel.onEvent(
                MainEvent.OnChangeDoubleClickTranslation(
                    !state.value.doubleClickTranslation
                )
            )
        }
    )
}