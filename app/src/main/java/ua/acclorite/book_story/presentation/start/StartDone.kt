/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.start

import androidx.compose.runtime.Composable

@Composable
fun StartDone(
    navigateToBrowse: () -> Unit,
    navigateToHelp: () -> Unit
) {
    StartDoneScaffold(
        navigateToBrowse = navigateToBrowse,
        navigateToHelp = navigateToHelp
    )
}