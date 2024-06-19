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
fun LazyItemScope.HelpCustomizeApp(
    state: State<HelpState>,
    onNavigate: OnNavigate,
    onEvent: (HelpEvent) -> Unit
) {
    HelpItem(
        title = stringResource(id = R.string.help_title_how_to_customize_app),
        description = buildAnnotatedString {
            append(stringResource(id = R.string.help_desc_how_to_customize_app_1) + " ")

            HelpAnnotation(tag = "settings") {
                append(stringResource(id = R.string.help_desc_how_to_customize_app_2))
            }
            append(". ")

            append(stringResource(id = R.string.help_desc_how_to_customize_app_3))
        },
        tags = listOf("settings"),
        shouldShowDescription = state.value.showHelpItem3,
        onTitleClick = {
            onEvent(
                HelpEvent.OnUpdateState {
                    it.copy(
                        showHelpItem3 = !it.showHelpItem3
                    )
                }
            )
        },
        onTagClick = { tag ->
            onNavigate {
                when (tag) {
                    "settings" -> {
                        if (!state.value.fromStart) {
                            navigate(Screen.Settings, useBackAnimation = true)
                        }
                    }
                }
            }
        }
    )
}