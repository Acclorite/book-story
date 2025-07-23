/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.progress.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem
import ua.acclorite.book_story.ui.theme.ExpandingTransition
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment

@Composable
fun ProgressBarAlignmentOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.progressBar.value) {
        SegmentedButtonWithTitle(
            title = stringResource(id = R.string.progress_bar_alignment_option),
            buttons = HorizontalAlignment.entries.map { item ->
                ListItem(
                    item = item,
                    title = stringResource(id = item.title),
                    selected = item == settings.progressBarAlignment.value
                )
            },
            onClick = { item ->
                settings.progressBarAlignment.update(item)
            }
        )
    }
}