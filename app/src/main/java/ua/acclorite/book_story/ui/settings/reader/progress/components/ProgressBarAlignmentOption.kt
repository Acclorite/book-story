/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.progress.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ButtonItem
import ua.acclorite.book_story.ui.theme.ExpandingTransition
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment

@Composable
fun ProgressBarAlignmentOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.progressBar.value) {
        SegmentedButtonWithTitle(
            title = stringResource(id = R.string.progress_bar_alignment_option),
            buttons = HorizontalAlignment.entries.map {
                ButtonItem(
                    id = it.name,
                    title = when (it) {
                        HorizontalAlignment.START -> stringResource(id = R.string.alignment_start)
                        HorizontalAlignment.CENTER -> stringResource(id = R.string.alignment_center)
                        HorizontalAlignment.END -> stringResource(id = R.string.alignment_end)
                    },
                    textStyle = MaterialTheme.typography.labelLarge,
                    selected = it == settings.progressBarAlignment.value
                )
            },
            onClick = {
                settings.progressBarAlignment.update(HorizontalAlignment.valueOf(it.id))
            }
        )
    }
}