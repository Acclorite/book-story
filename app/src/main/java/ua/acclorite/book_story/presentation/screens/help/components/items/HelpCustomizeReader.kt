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
fun LazyItemScope.HelpCustomizeReader(viewModel: HelpViewModel, navigator: Navigator) {
    val state by viewModel.state.collectAsState()

    HelpItem(
        title = stringResource(id = R.string.help_title_how_to_customize_reader),
        description = buildAnnotatedString {
            append(stringResource(id = R.string.help_desc_how_to_customize_reader_1) + " ")

            HelpAnnotation(tag = "settings") {
                append(stringResource(id = R.string.help_desc_how_to_customize_reader_2))
            }
            append(" ")

            append(stringResource(id = R.string.help_desc_how_to_customize_reader_3))
        },
        tags = listOf("settings"),
        shouldShowDescription = state.showHelpItem7,
        onTitleClick = {
            viewModel.onUpdate {
                it.copy(
                    showHelpItem7 = !it.showHelpItem7
                )
            }
        },
        onTagClick = { tag ->
            when (tag) {
                "settings" -> {
                    navigator.navigate(Screen.SETTINGS, true)
                }
            }
        }
    )
}