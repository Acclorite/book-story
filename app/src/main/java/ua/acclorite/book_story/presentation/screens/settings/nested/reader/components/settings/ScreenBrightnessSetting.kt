package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.settings.SliderWithTitle
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel

/**
 * Screen Brightness setting.
 * Changes screen brightness in Reader.
 */
@Composable
fun ScreenBrightnessSetting() {
    val state = MainViewModel.getState()
    val onMainEvent = MainViewModel.getEvent()

    SliderWithTitle(
        value = state.value.screenBrightness to "",
        toValue = 100,
        title = stringResource(id = R.string.screen_brightness_option),
        onValueChange = {
            onMainEvent(
                MainEvent.OnChangeScreenBrightness(it)
            )
        }
    )
}