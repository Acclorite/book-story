/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.progress.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderProgressCount
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem

@Composable
fun ProgressCountOption() {
    val settings = LocalSettings.current

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.progress_count_option),
        buttons = ReaderProgressCount.entries.map { item ->
            ListItem(
                item = item,
                title = stringResource(id = item.title),
                selected = item == settings.progressCount.value
            )
        },
        onClick = { item ->
            settings.progressCount.update(item)
        }
    )
}