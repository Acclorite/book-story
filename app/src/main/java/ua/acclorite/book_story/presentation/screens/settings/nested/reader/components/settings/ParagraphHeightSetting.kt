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
 * Paragraph Height setting.
 * Changes Reader's paragraph height.
 */
@Composable
fun LazyItemScope.ParagraphHeightSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SliderWithTitle(
        value = state.value.paragraphHeight!! to "pt",
        modifier = Modifier.animateItem(),
        fromValue = 0,
        toValue = 24,
        title = stringResource(id = R.string.paragraph_height_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangeParagraphHeight(it)
            )
        }
    )
}