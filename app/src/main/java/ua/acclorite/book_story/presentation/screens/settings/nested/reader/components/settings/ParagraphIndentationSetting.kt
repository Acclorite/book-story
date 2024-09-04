package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Paragraph Indentation setting.
 * Changes Reader's paragraph indentation.
 */
@Composable
fun ParagraphIndentationSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SwitchWithTitle(
        selected = state.value.paragraphIndentation,
        title = stringResource(id = R.string.paragraph_indentation_option)
    ) {
        onMainEvent(
            MainEvent.OnChangeParagraphIndentation(!state.value.paragraphIndentation)
        )
    }
}