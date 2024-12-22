package ua.acclorite.book_story.presentation.settings.reader.misc.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.settings.SwitchWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun CheckForTextUpdateToastOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(visible = state.value.checkForTextUpdate) {
        SwitchWithTitle(
            selected = state.value.checkForTextUpdateToast,
            title = stringResource(id = R.string.check_for_text_update_toast_option),
            onClick = {
                mainModel.onEvent(
                    MainEvent.OnChangeCheckForTextUpdateToast(
                        !state.value.checkForTextUpdateToast
                    )
                )
            }
        )
    }
}