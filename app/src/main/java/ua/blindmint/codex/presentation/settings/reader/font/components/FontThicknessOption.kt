/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.settings.reader.font.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.reader.ReaderFontThickness
import ua.blindmint.codex.domain.ui.ButtonItem
import ua.blindmint.codex.presentation.core.components.settings.ChipsWithTitle
import ua.blindmint.codex.presentation.core.constants.provideFonts
import ua.blindmint.codex.ui.main.MainEvent
import ua.blindmint.codex.ui.main.MainModel

@Composable
fun FontThicknessOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    val fontFamily = remember(state.value.fontFamily) {
        provideFonts().run {
            find {
                it.id == state.value.fontFamily
            } ?: get(0)
        }
    }

    ChipsWithTitle(
        title = stringResource(id = R.string.font_thickness_option),
        chips = ReaderFontThickness.entries.map {
            ButtonItem(
                id = it.toString(),
                title = when (it) {
                    ReaderFontThickness.THIN -> stringResource(id = R.string.font_thickness_thin)
                    ReaderFontThickness.EXTRA_LIGHT -> stringResource(id = R.string.font_thickness_extra_light)
                    ReaderFontThickness.LIGHT -> stringResource(id = R.string.font_thickness_light)
                    ReaderFontThickness.NORMAL -> stringResource(id = R.string.font_thickness_normal)
                    ReaderFontThickness.MEDIUM -> stringResource(id = R.string.font_thickness_medium)
                },
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = fontFamily.font,
                    fontWeight = it.thickness
                ),
                selected = it == state.value.fontThickness
            )
        },
        onClick = {
            mainModel.onEvent(
                MainEvent.OnChangeFontThickness(
                    it.id
                )
            )
        }
    )
}