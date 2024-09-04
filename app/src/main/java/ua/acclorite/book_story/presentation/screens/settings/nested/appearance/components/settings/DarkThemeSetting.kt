package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SegmentedButtonWithTitle
import ua.acclorite.book_story.presentation.ui.DarkTheme

/**
 * Dark Theme setting.
 * If true, dark theme applied to the app's theme.
 */
@Composable
fun DarkThemeSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.dark_theme_option),
        buttons = DarkTheme.entries.map {
            ButtonItem(
                it.toString(),
                title = when (it) {
                    DarkTheme.OFF -> stringResource(id = R.string.dark_theme_off)
                    DarkTheme.ON -> stringResource(id = R.string.dark_theme_on)
                    DarkTheme.FOLLOW_SYSTEM -> stringResource(id = R.string.dark_theme_follow_system)
                },
                textStyle = MaterialTheme.typography.labelLarge,
                selected = it == state.value.darkTheme
            )
        }
    ) {
        onMainEvent(
            MainEvent.OnChangeDarkTheme(
                it.id
            )
        )
    }
}