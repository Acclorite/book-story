/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.presentation.browse.model.BrowseLayout
import ua.acclorite.book_story.presentation.browse.model.SelectableFile

@Composable
fun BrowseItem(
    modifier: Modifier = Modifier,
    layout: BrowseLayout,
    file: SelectableFile,
    hasSelectedItems: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    when (layout) {
        BrowseLayout.LIST -> {
            BrowseListItem(
                modifier = modifier,
                file = file,
                hasSelectedItems = hasSelectedItems,
                onClick = onClick,
                onLongClick = onLongClick
            )
        }

        BrowseLayout.GRID -> {
            BrowseGridItem(
                modifier = modifier,
                file = file,
                hasSelectedItems = hasSelectedItems,
                onClick = onClick,
                onLongClick = onLongClick
            )
        }
    }
}