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
 * Font Size setting.
 * Changes Reader's font size.
 */
@Composable
fun LazyItemScope.FontSizeSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SliderWithTitle(
        value = state.value.fontSize!! to "pt",
        modifier = Modifier.animateItem(),
        fromValue = 10,
        toValue = 35,
        title = stringResource(id = R.string.font_size_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangeFontSize(it)
            )
        }
    )
}