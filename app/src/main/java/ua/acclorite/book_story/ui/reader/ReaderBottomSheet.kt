/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.core.BottomSheet
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.presentation.reader.ReaderScreen

@Composable
fun ReaderBottomSheet(
    bottomSheet: BottomSheet?,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit,
    dismissBottomSheet: (ReaderEvent.OnDismissBottomSheet) -> Unit
) {
    when (bottomSheet) {
        ReaderScreen.SETTINGS_BOTTOM_SHEET -> {
            ReaderSettingsBottomSheet(
                menuVisibility = menuVisibility,
                dismissBottomSheet = dismissBottomSheet
            )
        }
    }
}