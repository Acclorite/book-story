package ua.acclorite.book_story.presentation.screens.settings.nested.general.components.settings

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Double Press Exit setting.
 * If true, to close the app you need to press back twice.
 */
@SuppressLint("InlinedApi")
@Composable
fun DoublePressExitSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SwitchWithTitle(
        selected = state.value.doublePressExit,
        title = stringResource(id = R.string.double_press_exit_option),
        description = stringResource(id = R.string.double_press_exit_option_desc)
    ) {
        onMainEvent(
            MainEvent.OnChangeDoublePressExit(!state.value.doublePressExit)
        )
    }
}