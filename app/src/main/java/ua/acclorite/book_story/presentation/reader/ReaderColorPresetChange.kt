package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import ua.acclorite.book_story.ui.settings.SettingsEvent

@Composable
fun Modifier.readerColorPresetChange(
    colorPresetChangeEnabled: Boolean,
    isLoading: Boolean,
    selectPreviousPreset: (SettingsEvent.OnSelectPreviousPreset) -> Unit,
    selectNextPreset: (SettingsEvent.OnSelectNextPreset) -> Unit
): Modifier {
    val context = LocalContext.current
    val offset = remember { mutableFloatStateOf(0f) }
    return this.then(
        if (colorPresetChangeEnabled && !isLoading) {
            Modifier.pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { offset.floatValue = 0f },
                    onDragEnd = {
                        when {
                            offset.floatValue > 200 -> {
                                selectPreviousPreset(
                                    SettingsEvent.OnSelectPreviousPreset(
                                        context = context
                                    )
                                )
                            }

                            offset.floatValue < -200 -> {
                                selectNextPreset(
                                    SettingsEvent.OnSelectNextPreset(
                                        context = context
                                    )
                                )
                            }
                        }
                    }
                ) { _, dragAmount ->
                    offset.floatValue += dragAmount
                }
            }
        } else Modifier
    )
}