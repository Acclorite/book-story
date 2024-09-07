package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

/**
 * Cutout Padding setting.
 * If true, applies padding to cutout area.
 */
@Composable
fun CutoutPaddingSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SwitchWithTitle(
        selected = state.value.cutoutPadding,
        title = stringResource(id = R.string.cutout_padding_option),
        description = stringResource(id = R.string.cutout_padding_option_desc),
        onClick = {
            onMainEvent(
                MainEvent.OnChangeCutoutPadding(
                    !state.value.cutoutPadding
                )
            )
        }
    )
}