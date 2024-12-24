package ua.acclorite.book_story.presentation.settings.reader.reading_mode.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.reader.ReaderHorizontalGesture
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.core.components.settings.ChipsWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun HorizontalGestureOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ChipsWithTitle(
        title = stringResource(id = R.string.horizontal_gesture_option),
        chips = ReaderHorizontalGesture.entries.map {
            ButtonItem(
                id = it.toString(),
                title = when (it) {
                    ReaderHorizontalGesture.OFF -> {
                        stringResource(R.string.horizontal_gesture_off)
                    }

                    ReaderHorizontalGesture.ON -> {
                        stringResource(R.string.horizontal_gesture_on)
                    }

                    ReaderHorizontalGesture.INVERSE -> {
                        stringResource(R.string.horizontal_gesture_inverse)
                    }
                },
                textStyle = MaterialTheme.typography.labelLarge,
                selected = it == state.value.horizontalGesture
            )
        },
        onClick = {
            mainModel.onEvent(
                MainEvent.OnChangeHorizontalGesture(
                    it.id
                )
            )
        }
    )
}