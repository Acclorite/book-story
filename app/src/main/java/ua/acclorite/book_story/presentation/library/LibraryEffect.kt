/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.library

import androidx.compose.runtime.Immutable

@Immutable
sealed class LibraryEffect {
    data object OnRequestFocus : LibraryEffect()
    data object OnBooksMoved : LibraryEffect()
    data object OnBooksDeleted : LibraryEffect()
    data object OnNavigateToLibrarySettings : LibraryEffect()
    data object OnNavigateToBrowse : LibraryEffect()
    data class OnNavigateToBookInfo(val id: Int) : LibraryEffect()
    data class OnNavigateToReader(val id: Int) : LibraryEffect()
}