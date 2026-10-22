/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.ui.common.components.progress_indicator.CircularProgressIndicator
import ua.acclorite.book_story.ui.common.components.top_bar.TopAppBar
import ua.acclorite.book_story.ui.common.components.top_bar.TopAppBarData
import ua.acclorite.book_story.ui.navigator.NavigatorBackIconButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderLoadingPlaceholder(
    navigateBack: (ReaderEvent.OnNavigateBack) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                containerColor = Color.Transparent,
                scrollBehavior = null,
                isTopBarScrolled = null,

                shownTopBar = 0,
                topBars = listOf(
                    TopAppBarData(
                        contentID = 0,
                        contentNavigationIcon = {
                            NavigatorBackIconButton {
                                navigateBack(ReaderEvent.OnNavigateBack)
                            }
                        },
                        contentTitle = {},
                        contentActions = {}
                    )
                )
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.5.dp
            )
        }
    }
}