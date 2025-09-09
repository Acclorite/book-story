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
import ua.blindmint.codex.presentation.core.components.settings.SwitchWithTitle
import ua.blindmint.codex.ui.main.MainEvent
import ua.blindmint.codex.ui.main.MainModel
import ua.blindmint.codex.ui.theme.ExpandingTransition

@Composable
fun HorizontalGesturePullAnimOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(
        visible = when (state.value.horizontalGesture) {
            ReaderHorizontalGesture.OFF -> false
            else -> true
        }
    ) {
        SwitchWithTitle(
            selected = state.value.horizontalGesturePullAnim,
            title = stringResource(id = R.string.horizontal_gesture_pull_anim_option),
            description = stringResource(id = R.string.horizontal_gesture_pull_anim_option_desc),
            onClick = {
                mainModel.onEvent(
                    MainEvent.OnChangeHorizontalGesturePullAnim(
                        !state.value.horizontalGesturePullAnim
                    )
                )
            }
        )
    }
}