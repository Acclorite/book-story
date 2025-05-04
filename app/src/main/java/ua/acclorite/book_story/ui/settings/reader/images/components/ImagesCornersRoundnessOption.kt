/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.images.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.main.MainEvent
import ua.acclorite.book_story.presentation.main.MainModel
import ua.acclorite.book_story.presentation.theme.ExpandingTransition
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle

@Composable
fun ImagesCornersRoundnessOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(visible = state.value.images) {
        SliderWithTitle(
            value = state.value.imagesCornersRoundness to "pt",
            fromValue = 0,
            toValue = 24,
            title = stringResource(id = R.string.images_corners_roundness_option),
            onValueChange = {
                mainModel.onEvent(
                    MainEvent.OnChangeImagesCornersRoundness(it)
                )
            }
        )
    }
}