/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.book_info

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.domain.model.library.Category

@Immutable
sealed class BookInfoEvent {
    data object OnShowDetailsBottomSheet : BookInfoEvent()

    data object OnShowChangeCoverBottomSheet : BookInfoEvent()

    data class OnChangeCover(
        val image: Bitmap
    ) : BookInfoEvent()

    data object OnResetCover : BookInfoEvent()

    data object OnDeleteCover : BookInfoEvent()

    data object OnCheckCoverReset : BookInfoEvent()

    data object OnDismissBottomSheet : BookInfoEvent()

    data object OnShowTitleDialog : BookInfoEvent()

    data class OnActionTitleDialog(
        val title: String
    ) : BookInfoEvent()

    data object OnShowAuthorDialog : BookInfoEvent()

    data class OnActionAuthorDialog(
        val author: UIText
    ) : BookInfoEvent()

    data object OnShowDescriptionDialog : BookInfoEvent()

    data class OnActionDescriptionDialog(
        val description: String?
    ) : BookInfoEvent()

    data object OnShowPathDialog : BookInfoEvent()

    data class OnActionPathDialog(
        val path: String
    ) : BookInfoEvent()

    data object OnShowDeleteDialog : BookInfoEvent()

    data object OnActionDeleteDialog : BookInfoEvent()

    data object OnShowMoveDialog : BookInfoEvent()

    data class OnActionMoveDialog(
        val selectedCategories: List<Category>
    ) : BookInfoEvent()

    data object OnDismissDialog : BookInfoEvent()

    data object OnNavigateBack : BookInfoEvent()

    data object OnNavigateToLibrarySettings : BookInfoEvent()

    data object OnNavigateToReader : BookInfoEvent()
}