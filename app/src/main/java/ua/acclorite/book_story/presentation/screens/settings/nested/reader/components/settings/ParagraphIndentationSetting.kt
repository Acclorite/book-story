package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.data.ReaderTextAlignment
import ua.acclorite.book_story.presentation.ui.ExpandingTransition

/**
 * Paragraph Indentation setting.
 * Changes Reader's paragraph indentation.
 */
@Composable
fun ParagraphIndentationSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    ExpandingTransition(
        visible = state.value.textAlignment != ReaderTextAlignment.CENTER &&
                state.value.textAlignment != ReaderTextAlignment.END
    ) {
        SliderWithTitle(
            value = state.value.paragraphIndentation to "pt",
            fromValue = 0,
            toValue = 12,
            title = stringResource(id = R.string.paragraph_indentation_option),
            onValueChange = {
                onMainEvent(
                    MainEvent.OnChangeParagraphIndentation(it)
                )
            }
        )
    }
}