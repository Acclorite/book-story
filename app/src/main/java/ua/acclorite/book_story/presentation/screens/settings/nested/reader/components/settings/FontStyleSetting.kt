package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.components.SegmentedButtonWithTitle

/**
 * Font Style setting.
 * Changes Reader's font style (Normal/Italic).
 */
@Composable
fun LazyItemScope.FontStyleSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    val fontFamily = remember(state.value.fontFamily) {
        Constants.FONTS.find {
            it.id == state.value.fontFamily
        } ?: Constants.FONTS[0]
    }

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.font_style_option),
        modifier = Modifier.animateItem(),
        buttons = listOf(
            ButtonItem(
                id = "normal",
                title = stringResource(id = R.string.font_style_normal),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = fontFamily.font,
                    fontStyle = FontStyle.Normal
                ),
                selected = !state.value.isItalic!!
            ),
            ButtonItem(
                id = "italic",
                title = stringResource(id = R.string.font_style_italic),
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = fontFamily.font,
                    fontStyle = FontStyle.Italic
                ),
                selected = state.value.isItalic!!
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