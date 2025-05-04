/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.start

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun StartBackHandler(
    navigateBack: () -> Unit
) {
    BackHandler {
        navigateBack()
    }
}