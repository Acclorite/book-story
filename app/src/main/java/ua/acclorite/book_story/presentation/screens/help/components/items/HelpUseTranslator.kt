package ua.acclorite.book_story.presentation.screens.help.components.items

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.screens.help.components.HelpItem
import ua.acclorite.book_story.presentation.screens.help.data.HelpEvent
import ua.acclorite.book_story.presentation.screens.help.data.HelpState

@Composable
fun LazyItemScope.HelpUseTranslator(
    state: State<HelpState>,
    onEvent: (HelpEvent) -> Unit
) {
    HelpItem(
        title = stringResource(id = R.string.help_title_how_to_use_translator),
        description = buildAnnotatedString {
            append(stringResource(id = R.string.help_desc_how_to_use_translator_1))
        },
        tags = emptyList(),
        shouldShowDescription = state.value.showHelpItem12,
        onTitleClick = {
            onEvent(
                HelpEvent.OnUpdateState {
                    it.copy(
                        showHelpItem12 = !it.showHelpItem12
                    )
                }
            )
        },
        onTagClick = {}
    )
}