package ua.acclorite.book_story.presentation.settings.reader.reading_speed.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun HighlightedReadingThicknessOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(visible = state.value.highlightedReading) {
        SliderWithTitle(
            value = state.value.highlightedReadingThickness.to(
                " ${stringResource(R.string.highlighted_reading_level)}"
            ),
            fromValue = 1,
            toValue = 3,
            title = stringResource(id = R.string.highlighted_reading_thickness_option),
            onValueChange = {
                mainModel.onEvent(
                    MainEvent.OnChangeHighlightedReadingThickness(it)
                )
            }
        )
    }
}