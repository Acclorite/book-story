package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SegmentedButtonWithTitle

/**
 * Font Style setting.
 * Changes Reader's font style (Normal/Italic).
 */
@Composable
fun FontStyleSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    val fontFamily = remember(state.value.fontFamily) {
        Constants.FONTS.find {
            it.id == state.value.fontFamily
        } ?: Constants.FONTS[0]
    }

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.font_style_option),
        buttons = listOf(
            ButtonItem(
                id = "normal",
                title = stringResource(id = R.string.font_style_normal),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = fontFamily.font,
                    fontStyle = FontStyle.Normal
                ),
                selected = !state.value.isItalic
            ),
            ButtonItem(
                id = "italic",
                title = stringResource(id = R.string.font_style_italic),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = fontFamily.font,
                    fontStyle = FontStyle.Italic
                ),
                selected = state.value.isItalic
            ),
        ),
        onClick = {
            onMainEvent(
                MainEvent.OnChangeFontStyle(
                    when (it.id) {
                        "italic" -> true
                        else -> false
                    }
                )
            )
        }
    )
}