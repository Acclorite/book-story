package ua.acclorite.book_story.presentation.screens.help.components.items

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation
import ua.acclorite.book_story.presentation.screens.help.components.HelpItem
import ua.acclorite.book_story.presentation.screens.help.data.HelpViewModel

@Composable
fun LazyItemScope.HelpUpdateBook(viewModel: HelpViewModel, navigator: Navigator) {
    val state by viewModel.state.collectAsState()

    HelpItem(
        title = stringResource(id = R.string.help_title_how_to_update_book),
        description = buildAnnotatedString {
            append(stringResource(id = R.string.help_desc_how_to_update_book_1) + " ")

            HelpAnnotation(tag = "library") {
                append(stringResource(id = R.string.help_desc_how_to_update_book_2))
            }
            append(" ")

            append(stringResource(id = R.string.help_desc_how_to_update_book_3))
        },
        tags = listOf("library"),
        shouldShowDescription = state.showHelpItem8,
        onTitleClick = {
            viewModel.onUpdate {
                it.copy(
                    showHelpItem8 = !it.showHelpItem8
                )
            }
        },
        onTagClick = { tag ->
            when (tag) {
                "library" -> {
                    navigator.navigate(Screen.LIBRARY, true)
                }
            }
        }
    )
}