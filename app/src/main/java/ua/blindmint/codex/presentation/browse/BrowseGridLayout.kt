/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.domain.browse.file.GroupedFiles
import ua.blindmint.codex.domain.browse.file.SelectableFile
import ua.blindmint.codex.presentation.core.components.common.LazyVerticalGridWithScrollbar
import ua.blindmint.codex.presentation.core.components.common.header
import ua.blindmint.codex.presentation.core.constants.providePrimaryScrollbar

@Composable
fun BrowseGridLayout(
    groupedFiles: List<GroupedFiles>,
    gridSize: Int,
    autoGridSize: Boolean,
    gridState: LazyGridState,
    headerContent: @Composable (header: String, pinned: Boolean) -> Unit,
    itemContent: @Composable (file: SelectableFile, files: List<SelectableFile>) -> Unit
) {
    LazyVerticalGridWithScrollbar(
        columns = if (autoGridSize) GridCells.Adaptive(170.dp)
        else GridCells.Fixed(gridSize.coerceAtLeast(1)),
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        scrollbarSettings = providePrimaryScrollbar(false)
    ) {
        groupedFiles.forEach { group ->
            stickyHeader {
                Box(Modifier.animateItem()) {
                    headerContent(group.header, group.pinned)
                }
            }

            items(
                group.files,
                key = { it.data.path }
            ) { selectableFile ->
                Box(Modifier.animateItem()) {
                    itemContent(selectableFile, group.files)
                }
            }
        }

        header {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}