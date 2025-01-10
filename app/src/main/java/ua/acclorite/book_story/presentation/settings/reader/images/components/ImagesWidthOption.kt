package ua.acclorite.book_story.presentation.settings.reader.images.components

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
fun ImagesWidthOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(visible = state.value.images) {
        SliderWithTitle(
            value = state.value.imagesWidth to "%",
            toValue = 100,
            title = stringResource(id = R.string.images_width_option),
            onValueChange = {
                mainModel.onEvent(
                    MainEvent.OnChangeImagesWidth(it)
                )
            }
        )
    }
}