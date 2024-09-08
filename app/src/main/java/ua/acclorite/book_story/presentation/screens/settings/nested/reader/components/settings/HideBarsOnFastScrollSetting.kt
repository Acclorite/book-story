package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Hide Bars On Fast Scroll setting.
 * If true, hides bars during fast scroll.
 */
@Composable
fun HideBarsOnFastScrollSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SwitchWithTitle(
        selected = state.value.hideBarsOnFastScroll,
        title = stringResource(id = R.string.hide_bars_on_fast_scroll_option)
    ) {
        onMainEvent(
            MainEvent.OnChangeHideBarsOnFastScroll(!state.value.hideBarsOnFastScroll)
        )
    }
}