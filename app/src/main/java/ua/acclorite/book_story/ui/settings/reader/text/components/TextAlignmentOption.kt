/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.text.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ButtonItem

@Composable
fun TextAlignmentOption() {
    val settings = LocalSettings.current

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.text_alignment_option),
        buttons = ReaderTextAlignment.entries.map {
            ButtonItem(
                id = it.toString(),
                title = when (it) {
                    ReaderTextAlignment.START -> stringResource(id = R.string.alignment_start)
                    ReaderTextAlignment.JUSTIFY -> stringResource(id = R.string.alignment_justify)
                    ReaderTextAlignment.CENTER -> stringResource(id = R.string.alignment_center)
                    ReaderTextAlignment.END -> stringResource(id = R.string.alignment_end)
                },
                textStyle = MaterialTheme.typography.labelLarge,
                selected = it == settings.textAlignment.value
            )
        },
        onClick = {
            settings.textAlignment.update(ReaderTextAlignment.valueOf(it.id))
        }
    )
}