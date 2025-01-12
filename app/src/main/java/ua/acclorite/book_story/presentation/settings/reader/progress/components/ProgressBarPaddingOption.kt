package ua.acclorite.book_story.presentation.settings.reader.progress.components

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
fun ProgressBarPaddingOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(visible = state.value.progressBar) {
        SliderWithTitle(
            value = state.value.progressBarPadding to "pt",
            fromValue = 1,
            toValue = 12,
            title = stringResource(id = R.string.progress_bar_padding_option),
            onValueChange = {
                mainModel.onEvent(
                    MainEvent.OnChangeProgressBarPadding(it)
                )
            }
        )
    }
}