/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.browse

import androidx.compose.runtime.Immutable

@Immutable
sealed class BrowseEffect {
    data object OnRequestFocus : BrowseEffect()

    data object OnBooksAdded : BrowseEffect()

    data object OnNavigateToLibrary : BrowseEffect()

    data class OnUpdatePinnedPaths(
        val path: String
    ) : BrowseEffect()

    data object OnNavigateToBrowseSettings : BrowseEffect()
}