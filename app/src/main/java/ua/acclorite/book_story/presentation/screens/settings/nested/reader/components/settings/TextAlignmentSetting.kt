package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

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
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.data.ReaderTextAlignment

/**
 * Text Alignment setting.
 * Changes Reader's text alignment (Start/Justify/Center/End).
 */
@Composable
fun LazyItemScope.TextAlignmentSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.text_alignment_option),
        modifier = Modifier.animateItem(),
        buttons = ReaderTextAlignment.entries.map {
            ButtonItem(
                id = it.toString(),
                title = when (it) {
                    ReaderTextAlignment.START -> stringResource(id = R.string.text_alignment_start)
                    ReaderTextAlignment.JUSTIFY -> stringResource(id = R.string.text_alignment_justify)
                    ReaderTextAlignment.CENTER -> stringResource(id = R.string.text_alignment_center)
                    ReaderTextAlignment.END -> stringResource(id = R.string.text_alignment_end)
                },
                textStyle = MaterialTheme.typography.labelLarge,
                selected = it == state.value.textAlignment!!
            )
        },
        onClick = {
            onMainEvent(
                MainEvent.OnChangeTextAlignment(
                    it.id
                )
            )
        }
    )
}