package ua.acclorite.book_story.presentation.settings.reader.padding.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun BottomBarPaddingOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SliderWithTitle(
        value = state.value.bottomBarPadding to "pt",
        fromValue = 0,
        toValue = 24,
        title = stringResource(id = R.string.bottom_bar_padding_option),
        onValueChange = {
            mainModel.onEvent(
                MainEvent.OnChangeBottomBarPadding(it)
            )
        }
    )
}