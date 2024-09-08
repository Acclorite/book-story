package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle
import ua.acclorite.book_story.presentation.ui.ExpandingTransition
import ua.acclorite.book_story.presentation.ui.isDark
import ua.acclorite.book_story.presentation.ui.isPureDark

/**
 * Absolute Dark setting.
 * If true, changes Pure Dark (OLED) theme to have black color as background.
 */
@Composable
fun AbsoluteDarkSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent
    val context = LocalContext.current

    ExpandingTransition(
        visible = state.value.pureDark.isPureDark(context)
                && state.value.darkTheme.isDark()
    ) {
        SwitchWithTitle(
            selected = state.value.absoluteDark,
            title = stringResource(id = R.string.absolute_dark_option),
            description = stringResource(id = R.string.absolute_dark_option_desc),
            onClick = {
                onMainEvent(
                    MainEvent.OnChangeAbsoluteDark(
                        !state.value.absoluteDark
                    )
                )
            }
        )
    }
}