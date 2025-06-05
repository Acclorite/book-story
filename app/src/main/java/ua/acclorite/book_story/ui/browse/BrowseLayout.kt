/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ua.acclorite.book_story.presentation.browse.model.BrowseLayout
import ua.acclorite.book_story.presentation.browse.model.GroupedFiles
import ua.acclorite.book_story.presentation.browse.model.SelectableFile
import java.io.File

@Composable
fun BrowseLayout(
    files: List<SelectableFile>,
    pinnedPaths: List<String>,
    layout: BrowseLayout,
    gridSize: Int,
    autoGridSize: Boolean,
    listState: LazyListState,
    gridState: LazyGridState,
    headerContent: @Composable (header: String, pinned: Boolean) -> Unit,
    itemContent: @Composable (file: SelectableFile, files: List<SelectableFile>) -> Unit
) {
    val groupedFiles = remember(files, pinnedPaths) {
        files.groupBy { file ->
            file.path.substringBeforeLast(File.separator)
        }.map { (header, files) ->
            GroupedFiles(
                header = header,
                pinned = pinnedPaths.any { it.lowercase().trim() == header.lowercase().trim() },
                files = files
            )
        }.sortedByDescending { group ->
            group.pinned
        }
    }

    when (layout) {
        BrowseLayout.LIST -> {
            BrowseListLayout(
                groupedFiles = groupedFiles,
                listState = listState,
                headerContent = headerContent,
                itemContent = itemContent
            )
        }

        BrowseLayout.GRID -> {
            BrowseGridLayout(
                groupedFiles = groupedFiles,
                gridSize = gridSize,
                autoGridSize = autoGridSize,
                gridState = gridState,
                headerContent = headerContent,
                itemContent = itemContent
            )
        }
    }
}