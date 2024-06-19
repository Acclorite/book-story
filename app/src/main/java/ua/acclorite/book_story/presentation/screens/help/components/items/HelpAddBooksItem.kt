package ua.acclorite.book_story.presentation.screens.help.components.items

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation
import ua.acclorite.book_story.presentation.screens.help.components.HelpItem
import ua.acclorite.book_story.presentation.screens.help.data.HelpEvent
import ua.acclorite.book_story.presentation.screens.help.data.HelpState

@Composable
fun LazyItemScope.HelpAddBooksItem(
    state: State<HelpState>,
    onNavigate: OnNavigate,
    onEvent: (HelpEvent) -> Unit
) {
    HelpItem(
        title = stringResource(id = R.string.help_title_how_to_add_books),
        description = buildAnnotatedString {
            append(stringResource(id = R.string.help_desc_how_to_add_books_1) + " ")

            HelpAnnotation(tag = "browse") {
                append(stringResource(id = R.string.help_desc_how_to_add_books_2))
            }
            append(". ")

            append(stringResource(id = R.string.help_desc_how_to_add_books_3) + " ")

            HelpAnnotation(tag = "library") {
                append(stringResource(id = R.string.help_desc_how_to_add_books_4))
            }
            append(".")
        },
        tags = listOf("browse", "library"),
        shouldShowDescription = state.value.showHelpItem2,
        onTitleClick = {
            onEvent(
                HelpEvent.OnUpdateState {
                    it.copy(
                        showHelpItem2 = !it.showHelpItem2
                    )
                }
            )
        },
        onTagClick = { tag ->
            onNavigate {
                when (tag) {
                    "browse" -> {
                        if (!state.value.fromStart) {
                            navigate(Screen.Browse, useBackAnimation = true)
                        }
                    }

                    "library" -> {
                        if (!state.value.fromStart) {
                            navigate(Screen.Library, useBackAnimation = true)
                        }
                    }
                }
            }
        }
    )
}