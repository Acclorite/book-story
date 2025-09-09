/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.library

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.presentation.core.components.common.LazyVerticalGridWithScrollbar
import ua.blindmint.codex.presentation.core.constants.providePrimaryScrollbar

@Composable
fun LibraryLayout(
    items: LazyGridScope.() -> Unit
) {
    LazyVerticalGridWithScrollbar(
        columns = GridCells.Adaptive(120.dp),
        modifier = Modifier.fillMaxSize(),
        scrollbarSettings = providePrimaryScrollbar(false),
        contentPadding = PaddingValues(8.dp)
    ) {
        items()
    }
}