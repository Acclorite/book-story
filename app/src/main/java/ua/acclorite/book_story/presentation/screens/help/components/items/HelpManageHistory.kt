package ua.acclorite.book_story.presentation.screens.help.components.items

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation
import ua.acclorite.book_story.presentation.screens.help.components.HelpItem
import ua.acclorite.book_story.presentation.screens.help.data.HelpEvent
import ua.acclorite.book_story.presentation.screens.help.data.HelpState

@Composable
fun LazyItemScope.HelpManageHistory(
    state: State<HelpState>,
    onEvent: (HelpEvent) -> Unit
) {
    val navigator = LocalNavigator.current

    HelpItem(
        title = stringResource(id = R.string.help_title_how_to_manage_history),
        description = buildAnnotatedString {
            append(stringResource(id = R.string.help_desc_how_to_manage_history_1) + " ")

            HelpAnnotation(tag = "history") {
                append(stringResource(id = R.string.help_desc_how_to_manage_history_2))
            }
            append(". ")

            append(stringResource(id = R.string.help_desc_how_to_manage_history_3))
        },
        tags = listOf("history"),
        shouldShowDescription = state.value.showHelpItem9,
        onTitleClick = {
            onEvent(
                HelpEvent.OnUpdateState {
                    it.copy(
                        showHelpItem9 = !it.showHelpItem9
                    )
                }
            )
        },
        onTagClick = { tag ->
            when (tag) {
                "history" -> {
                    if (!state.value.fromStart) {
                        navigator.navigate(Screen.HISTORY, true)
                    }
                }
            }
        }
    )
}