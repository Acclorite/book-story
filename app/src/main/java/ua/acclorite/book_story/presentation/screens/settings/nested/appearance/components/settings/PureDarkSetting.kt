package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.components.SegmentedButtonWithTitle
import ua.acclorite.book_story.presentation.ui.PureDark
import ua.acclorite.book_story.presentation.ui.SlidingTransition
import ua.acclorite.book_story.presentation.ui.isDark

/**
 * Pure Dark setting.
 * If true, enables Pure Dark (OLED) theme.
 */
@Composable
fun LazyItemScope.PureDarkSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SlidingTransition(
        modifier = Modifier.animateItem(
            fadeInSpec = null,
            fadeOutSpec = null
        ),
        visible = state.value.darkTheme!!.isDark(),
    ) {
        SegmentedButtonWithTitle(
            title = stringResource(id = R.string.pure_dark_option),
            buttons = PureDark.entries.map {
                ButtonItem(
                    id = it.toString(),
                    title = when (it) {
                        PureDark.OFF -> stringResource(id = R.string.pure_dark_off)
                        PureDark.ON -> stringResource(id = R.string.pure_dark_on)
                        PureDark.SAVER -> stringResource(id = R.string.pure_dark_power_saver)
                    },
                    textStyle = MaterialTheme.typography.labelLarge,
                    selected = it == state.value.pureDark
                )
            }
        ) {
            onMainEvent(
                MainEvent.OnChangePureDark(
                    it.id
                )
            )
        }
    }
}