/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.settings.reader.reading_mode.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.reader.ReaderHorizontalGesture
import ua.blindmint.codex.presentation.core.components.settings.SliderWithTitle
import ua.blindmint.codex.ui.main.MainEvent
import ua.blindmint.codex.ui.main.MainModel
import ua.blindmint.codex.ui.theme.ExpandingTransition

@Composable
fun HorizontalGestureScrollOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(
        visible = when (state.value.horizontalGesture) {
            ReaderHorizontalGesture.OFF -> false
            else -> true
        }
    ) {
        SliderWithTitle(
            value = state.value.horizontalGestureScroll to "%",
            toValue = 100,
            title = stringResource(id = R.string.horizontal_gesture_scroll_option),
            onValueChange = {
                mainModel.onEvent(
                    MainEvent.OnChangeHorizontalGestureScroll(it)
                )
            }
        )
    }
}