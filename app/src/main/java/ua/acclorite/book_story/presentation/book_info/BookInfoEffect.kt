/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.book_info

import androidx.compose.runtime.Immutable

@Immutable
sealed class BookInfoEffect {
    data object OnChangedCover : BookInfoEffect()

    data object OnErrorResetCover : BookInfoEffect()

    data object OnResetCover : BookInfoEffect()

    data object OnDeletedCover : BookInfoEffect()

    data object OnTitleChanged : BookInfoEffect()

    data object OnAuthorChanged : BookInfoEffect()

    data object OnDescriptionChanged : BookInfoEffect()

    data object OnPathChanged : BookInfoEffect()

    data object OnBookDeleted : BookInfoEffect()

    data object OnBookMoved : BookInfoEffect()

    data object OnNavigateBack : BookInfoEffect()

    data object OnNavigateToLibrarySettings : BookInfoEffect()

    data object OnNavigateToReader : BookInfoEffect()
}