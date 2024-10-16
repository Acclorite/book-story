package ua.acclorite.book_story.presentation.screens.reader.components

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsEvent
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsViewModel

/**
 * Reader Fast Color Preset Change.
 * Detects horizontal swipe gestures.
 *
 * @param fastColorPresetChangeEnabled Whether this is enabled.
 * @param isLoading Whether Reader is loading.
 * @param presetChanged Callback when Color Preset was changed.
 */
@Composable
fun Modifier.readerFastColorPresetChange(
    fastColorPresetChangeEnabled: Boolean,
    isLoading: Boolean,
    presetChanged: (UIText) -> Unit
): Modifier {
    val onSettingsEvent = SettingsViewModel.getEvent()

    val offset = remember { mutableFloatStateOf(0f) }
    return this.then(
        if (fastColorPresetChangeEnabled && !isLoading) {
            Modifier.pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { offset.floatValue = 0f },
                    onDragEnd = {
                        when {
                            offset.floatValue > 200 -> {
                                onSettingsEvent(
                                    SettingsEvent.OnSelectPreviousPreset {
                                        presetChanged(it)
                                    }
                                )
                            }

                            offset.floatValue < -200 -> {
                                onSettingsEvent(
                                    SettingsEvent.OnSelectNextPreset {
                                        presetChanged(it)
                                    }
                                )
                            }
                        }
                    }
                ) { _, dragAmount ->
                    offset.floatValue += dragAmount
                }
            }
        } else {
            Modifier
        }
    )
}