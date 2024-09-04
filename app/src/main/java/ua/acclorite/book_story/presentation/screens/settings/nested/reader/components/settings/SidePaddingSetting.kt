package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle

/**
 * Side Padding setting.
 * Changes Reader's side padding.
 */
@Composable
fun SidePaddingSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SliderWithTitle(
        value = state.value.sidePadding to "pt",
        fromValue = 1,
        toValue = 20,
        title = stringResource(id = R.string.side_padding_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangeSidePadding(it)
            )
        }
    )
}