package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle

/**
 * Side Padding setting.
 * Changes Reader's side padding.
 */
@Composable
fun LazyItemScope.SidePaddingSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SliderWithTitle(
        value = state.value.sidePadding!! to "pt",
        modifier = Modifier.animateItem(),
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