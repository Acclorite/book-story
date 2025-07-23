/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.chapters.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem

@Composable
fun ChapterTitleAlignmentOption() {
    val settings = LocalSettings.current

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.chapter_title_alignment_option),
        buttons = ReaderTextAlignment.entries.map { item ->
            ListItem(
                item = item,
                title = stringResource(id = item.title),
                selected = item == settings.chapterTitleAlignment.value
            )
        },
        onClick = { item ->
            settings.chapterTitleAlignment.update(item)
        }
    )
}