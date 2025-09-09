/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.settings.reader.progress.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.reader.ReaderProgressCount
import ua.blindmint.codex.domain.ui.ButtonItem
import ua.blindmint.codex.presentation.core.components.settings.SegmentedButtonWithTitle
import ua.blindmint.codex.ui.main.MainEvent
import ua.blindmint.codex.ui.main.MainModel

@Composable
fun ProgressCountOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.progress_count_option),
        buttons = ReaderProgressCount.entries.map {
            ButtonItem(
                id = it.toString(),
                title = when (it) {
                    ReaderProgressCount.PERCENTAGE -> stringResource(id = R.string.progress_count_percentage)
                    ReaderProgressCount.QUANTITY -> stringResource(id = R.string.progress_count_quantity)
                    ReaderProgressCount.PAGE -> stringResource(id = R.string.progress_count_page)
                },
                textStyle = MaterialTheme.typography.labelLarge,
                selected = it == state.value.progressCount
            )
        },
        onClick = {
            mainModel.onEvent(
                MainEvent.OnChangeProgressCount(
                    it.id
                )
            )
        }
    )
}