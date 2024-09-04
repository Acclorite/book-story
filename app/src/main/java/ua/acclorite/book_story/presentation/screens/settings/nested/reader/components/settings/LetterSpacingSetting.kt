package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle

/**
 * Letter Spacing setting.
 * Changes Reader's letter spacing.
 */
@Composable
fun LetterSpacingSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    SliderWithTitle(
        value = state.value.letterSpacing to "pt",
        fromValue = -8,
        toValue = 16,
        title = stringResource(id = R.string.letter_spacing_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangeLetterSpacing(it)
            )
        }
    )
}