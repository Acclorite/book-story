package ua.acclorite.book_story.presentation.settings.reader.font.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.core.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideFonts
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun FontStyleOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    val fontFamily = remember(state.value.fontFamily) {
        Constants.provideFonts(withRandom = false).find {
            it.id == state.value.fontFamily
        } ?: Constants.provideFonts(withRandom = false)[0]
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
            )
        ),
        onClick = {
            mainModel.onEvent(
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