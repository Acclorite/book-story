package ua.acclorite.book_story.presentation.screens.settings.nested.general.components.settings

import android.annotation.SuppressLint
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
 * Double Press Exit setting.
 * If true, to close the app you need to press back twice.
 */
@SuppressLint("InlinedApi")
@Composable
fun LazyItemScope.DoublePressExitSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SwitchWithTitle(
        selected = state.value.doublePressExit!!,
        modifier = Modifier.animateItem(),
        title = stringResource(id = R.string.double_press_exit_option),
        description = stringResource(id = R.string.double_press_exit_option_desc)
    ) {
        onMainEvent(
            MainEvent.OnChangeDoublePressExit(!state.value.doublePressExit!!)
        )
    }
}