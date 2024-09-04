package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle

/**
 * Line Height setting.
 * Changes Reader's line height.
 */
@Composable
fun LineHeightSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SliderWithTitle(
        value = state.value.lineHeight to "pt",
        fromValue = 1,
        toValue = 24,
        title = stringResource(id = R.string.line_height_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangeLineHeight(it)
            )
        }
    )
}