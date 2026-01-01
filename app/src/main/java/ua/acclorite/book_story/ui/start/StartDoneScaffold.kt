/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.start

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StartDoneScaffold(
    navigateToBrowse: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            StartDoneBottomBar(
                navigateToBrowse = navigateToBrowse
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        StartDoneLayout(
            paddingValues = it
        )
    }
}