package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle

/**
 * Paragraph Height setting.
 * Changes Reader's paragraph height.
 */
@Composable
fun ParagraphHeightSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SliderWithTitle(
        value = state.value.paragraphHeight to "pt",
        fromValue = 0,
        toValue = 36,
        title = stringResource(id = R.string.paragraph_height_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangeParagraphHeight(it)
            )
        }
    )
}