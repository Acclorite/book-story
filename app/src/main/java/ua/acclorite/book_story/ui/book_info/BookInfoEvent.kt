/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.domain.ui.UIText

@Immutable
sealed class BookInfoEvent {

    data object OnShowDetailsBottomSheet : BookInfoEvent()

    data object OnShowChangeCoverBottomSheet : BookInfoEvent()

    data class OnChangeCover(
        val uri: Uri,
        val context: Context
    ) : BookInfoEvent()

    data class OnResetCover(
        val context: Context
    ) : BookInfoEvent()

    data class OnDeleteCover(
        val context: Context
    ) : BookInfoEvent()

    data object OnCheckCoverReset : BookInfoEvent()

    data object OnDismissBottomSheet : BookInfoEvent()

    data object OnShowTitleDialog : BookInfoEvent()

    data class OnActionTitleDialog(
        val title: String,
        val context: Context
    ) : BookInfoEvent()

    data object OnShowAuthorDialog : BookInfoEvent()

    data class OnActionAuthorDialog(
        val author: UIText,
        val context: Context
    ) : BookInfoEvent()

    data object OnShowDescriptionDialog : BookInfoEvent()

    data class OnActionDescriptionDialog(
        val description: String?,
        val context: Context
    ) : BookInfoEvent()

    data object OnShowPathDialog : BookInfoEvent()

    data class OnActionPathDialog(
        val path: String,
        val context: Context
    ) : BookInfoEvent()

    data object OnShowDeleteDialog : BookInfoEvent()

    data class OnActionDeleteDialog(
        val context: Context,
        val navigateBack: () -> Unit
    ) : BookInfoEvent()

    data object OnShowMoveDialog : BookInfoEvent()

    data class OnActionMoveDialog(
        val category: Category,
        val context: Context,
        val navigateToLibrary: () -> Unit
    ) : BookInfoEvent()

    data object OnDismissDialog : BookInfoEvent()
}