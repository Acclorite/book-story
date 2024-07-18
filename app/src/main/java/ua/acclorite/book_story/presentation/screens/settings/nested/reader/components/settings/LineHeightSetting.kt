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
 * Line Height setting.
 * Changes Reader's line height.
 */
@Composable
fun LazyItemScope.LineHeightSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SliderWithTitle(
        value = state.value.lineHeight!! to "pt",
        modifier = Modifier.animateItem(),
        fromValue = 1,
        toValue = 16,
        title = stringResource(id = R.string.line_height_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangeLineHeight(it)
            )
        }
    )
}