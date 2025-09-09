/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.settings.reader.reading_speed.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.blindmint.codex.R
import ua.blindmint.codex.presentation.core.components.settings.SliderWithTitle
import ua.blindmint.codex.ui.main.MainEvent
import ua.blindmint.codex.ui.main.MainModel
import ua.blindmint.codex.ui.theme.ExpandingTransition

@Composable
fun HighlightedReadingThicknessOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(visible = state.value.highlightedReading) {
        SliderWithTitle(
            value = state.value.highlightedReadingThickness
                    to " ${stringResource(R.string.highlighted_reading_level)}",
            fromValue = 1,
            toValue = 3,
            title = stringResource(id = R.string.highlighted_reading_thickness_option),
            onValueChange = {
                mainModel.onEvent(
                    MainEvent.OnChangeHighlightedReadingThickness(it)
                )
            }
        )
    }
}