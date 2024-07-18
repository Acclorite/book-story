package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Paragraph Indentation setting.
 * Changes Reader's paragraph indentation.
 */
@Composable
fun LazyItemScope.ParagraphIndentationSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SwitchWithTitle(
        selected = state.value.paragraphIndentation!!,
        modifier = Modifier.animateItem(),
        title = stringResource(id = R.string.paragraph_indentation_option)
    ) {
        onMainEvent(
            MainEvent.OnChangeParagraphIndentation(!state.value.paragraphIndentation!!)
        )
    }
}