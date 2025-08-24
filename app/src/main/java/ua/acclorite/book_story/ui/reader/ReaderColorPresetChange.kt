/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import ua.acclorite.book_story.presentation.settings.SettingsEvent

@Composable
fun Modifier.readerColorPresetChange(
    colorPresetChangeEnabled: Boolean,
    isLoading: Boolean,
    switchColorPreset: (SettingsEvent.OnSwitchColorPreset) -> Unit
): Modifier {
    val offset = remember { mutableFloatStateOf(0f) }
    return this.then(
        if (colorPresetChangeEnabled && !isLoading) {
            Modifier.pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { offset.floatValue = 0f },
                    onDragCancel = { offset.floatValue = 0f },
                    onDragEnd = {
                        when {
                            offset.floatValue > 200 -> {
                                switchColorPreset(
                                    SettingsEvent.OnSwitchColorPreset(
                                        previous = true
                                    )
                                )
                            }

                            offset.floatValue < -200 -> {
                                switchColorPreset(
                                    SettingsEvent.OnSwitchColorPreset(
                                        previous = false
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