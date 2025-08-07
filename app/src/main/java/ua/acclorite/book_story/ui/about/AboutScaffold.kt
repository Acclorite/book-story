/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.about

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import ua.acclorite.book_story.presentation.about.AboutEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScaffold(
    scrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    navigateToLicenses: (AboutEvent.OnNavigateToLicenses) -> Unit,
    navigateToCredits: (AboutEvent.OnNavigateToCredits) -> Unit,
    navigateBack: (AboutEvent.OnNavigateBack) -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AboutTopBar(
                scrollBehavior = scrollBehavior,
                navigateBack = navigateBack
            )
        }
    ) { paddingValues ->
        AboutLayout(
            paddingValues = paddingValues,
            listState = listState,
            navigateToBrowserPage = navigateToBrowserPage,
            navigateToLicenses = navigateToLicenses,
            navigateToCredits = navigateToCredits
        )
    }
}