package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Fast Color Preset Change setting.
 * If true, user can fast change color presets in Reader with swipes.
 */
@Composable
fun FastColorPresetChangeSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SwitchWithTitle(
        selected = state.value.fastColorPresetChange,
        title = stringResource(id = R.string.fast_color_preset_change_option),
        description = stringResource(id = R.string.fast_color_preset_change_option_desc),
        onClick = {
            onMainEvent(
                MainEvent.OnChangeFastColorPresetChange(
                    !state.value.fastColorPresetChange
                )
            )
        }
    )
}