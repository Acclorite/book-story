/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.presentation.reader.ReaderEvent

@Composable
fun ReaderBackHandler(
    leave: (ReaderEvent.OnLeave) -> Unit,
    navigateBack: (ReaderEvent.OnNavigateBack) -> Unit
) {
    BackHandler {
        leave(
            ReaderEvent.OnLeave(
                navigate = {
                    navigateBack(ReaderEvent.OnNavigateBack)
                }
            )
        )
    }
}