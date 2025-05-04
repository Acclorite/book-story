/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.reading_mode.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.main.MainEvent
import ua.acclorite.book_story.presentation.main.MainModel
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.presentation.theme.ExpandingTransition
import ua.acclorite.book_story.ui.common.components.settings.SwitchWithTitle

@Composable
fun HorizontalGestureAlphaAnimOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(
        visible = when (state.value.horizontalGesture) {
            ReaderHorizontalGesture.OFF -> false
            else -> true
        }
    ) {
        SwitchWithTitle(
            selected = state.value.horizontalGestureAlphaAnim,
            title = stringResource(id = R.string.horizontal_gesture_alpha_anim_option),
            description = stringResource(id = R.string.horizontal_gesture_alpha_anim_option_desc),
            onClick = {
                mainModel.onEvent(
                    MainEvent.OnChangeHorizontalGestureAlphaAnim(
                        !state.value.horizontalGestureAlphaAnim
                    )
                )
            }
        )
    }
}