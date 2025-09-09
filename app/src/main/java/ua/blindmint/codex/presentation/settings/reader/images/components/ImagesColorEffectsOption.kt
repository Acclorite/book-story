/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.settings.reader.images.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.reader.ReaderColorEffects
import ua.blindmint.codex.domain.ui.ButtonItem
import ua.blindmint.codex.presentation.core.components.settings.ChipsWithTitle
import ua.blindmint.codex.ui.main.MainEvent
import ua.blindmint.codex.ui.main.MainModel
import ua.blindmint.codex.ui.theme.ExpandingTransition

@Composable
fun ImagesColorEffectsOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    ExpandingTransition(visible = state.value.images) {
        ChipsWithTitle(
            title = stringResource(id = R.string.images_color_effects_option),
            chips = ReaderColorEffects.entries.map {
                ButtonItem(
                    id = it.toString(),
                    title = when (it) {
                        ReaderColorEffects.OFF -> {
                            stringResource(R.string.color_effects_off)
                        }

                        ReaderColorEffects.GRAYSCALE -> {
                            stringResource(R.string.color_effects_grayscale)
                        }

                        ReaderColorEffects.FONT -> {
                            stringResource(R.string.color_effects_font)
                        }

                        ReaderColorEffects.BACKGROUND -> {
                            stringResource(R.string.color_effects_background)
                        }
                    },
                    textStyle = MaterialTheme.typography.labelLarge,
                    selected = it == state.value.imagesColorEffects
                )
            },
            onClick = {
                mainModel.onEvent(
                    MainEvent.OnChangeImagesColorEffects(
                        it.id
                    )
                )
            }
        )
    }
}