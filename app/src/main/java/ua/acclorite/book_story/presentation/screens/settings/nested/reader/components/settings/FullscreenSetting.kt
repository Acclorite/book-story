package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Fullscreen setting.
 * If true, hides system bars in Reader.
 */
@Composable
fun FullscreenSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SwitchWithTitle(
        selected = state.value.fullscreen,
        title = stringResource(id = R.string.fullscreen_option),
        description = stringResource(id = R.string.fullscreen_option_desc),
        onClick = {
            onMainEvent(
                MainEvent.OnChangeFullscreen(
                    !state.value.fullscreen
                )
            )
        }
    )
}