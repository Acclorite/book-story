/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.about

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.presentation.about.AboutEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutContent(
    scrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    navigateToLicenses: (AboutEvent.OnNavigateToLicenses) -> Unit,
    navigateToCredits: (AboutEvent.OnNavigateToCredits) -> Unit,
    navigateBack: (AboutEvent.OnNavigateBack) -> Unit
) {
    AboutScaffold(
        scrollBehavior = scrollBehavior,
        listState = listState,
        navigateToBrowserPage = navigateToBrowserPage,
        navigateToLicenses = navigateToLicenses,
        navigateToCredits = navigateToCredits,
        navigateBack = navigateBack
    )
}