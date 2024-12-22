package ua.acclorite.book_story.presentation.settings.reader.misc.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.settings.SwitchWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun FullscreenOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SwitchWithTitle(
        selected = state.value.fullscreen,
        title = stringResource(id = R.string.fullscreen_option),
        description = stringResource(id = R.string.fullscreen_option_desc),
        onClick = {
            mainModel.onEvent(
                MainEvent.OnChangeFullscreen(
                    !state.value.fullscreen
                )
            )
        }
    )
}