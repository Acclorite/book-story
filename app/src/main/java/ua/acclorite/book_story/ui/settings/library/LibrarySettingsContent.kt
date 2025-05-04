/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.library

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySettingsContent(
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateBack: () -> Unit
) {
    LibrarySettingsScaffold(
        listState = listState,
        scrollBehavior = scrollBehavior,
        navigateBack = navigateBack
    )
}