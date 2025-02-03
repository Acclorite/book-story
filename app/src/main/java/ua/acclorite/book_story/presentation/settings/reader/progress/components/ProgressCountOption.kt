/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings.reader.progress.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.reader.ReaderProgressCount
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.core.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

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