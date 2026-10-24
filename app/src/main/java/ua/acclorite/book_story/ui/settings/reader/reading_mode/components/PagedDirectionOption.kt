/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.reading_mode.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderLayout
import ua.acclorite.book_story.presentation.reader.model.ReaderPagedDirection
import ua.acclorite.book_story.ui.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun PagedDirectionOption() {
    val settings = LocalSettings.current

    ExpandingTransition(visible = settings.readerLayout.value == ReaderLayout.PAGED) {
        ChipsWithTitle(
            title = stringResource(R.string.paged_direction_option),
            chips = ReaderPagedDirection.entries.map { direction ->
                ListItem(
                    item = direction,
                    title = stringResource(direction.title),
                    selected = direction == settings.pagedDirection.value
                )
            },
            onClick = { settings.pagedDirection.update(it) }
        )
    }
}
