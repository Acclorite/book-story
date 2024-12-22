package ua.acclorite.book_story.ui.book_info

import android.content.Context
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.domain.reader.Chapter

@Immutable
sealed class BookInfoEvent {

    data class OnCopyToClipboard(
        val text: String,
        val context: Context,
    ) : BookInfoEvent()

    data class OnEditTitleMode(
        val edit: Boolean
    ) : BookInfoEvent()

    data class OnEditTitleRequestFocus(
        val focusRequester: FocusRequester
    ) : BookInfoEvent()

    data class OnEditTitleValueChange(
        val value: String
    ) : BookInfoEvent()

    data class OnEditAuthorMode(
        val edit: Boolean
    ) : BookInfoEvent()

    data class OnEditAuthorRequestFocus(
        val focusRequester: FocusRequester
    ) : BookInfoEvent()

    data class OnEditAuthorValueChange(
        val value: String
    ) : BookInfoEvent()

    data class OnEditDescriptionMode(
        val edit: Boolean
    ) : BookInfoEvent()

    data class OnEditDescriptionRequestFocus(
        val focusRequester: FocusRequester
    ) : BookInfoEvent()

    data class OnEditDescriptionValueChange(
        val value: String
    ) : BookInfoEvent()

    data object OnUpdateData : BookInfoEvent()

    data class OnShowSnackbar(
        val text: String,
        val action: String?,
        val onAction: () -> Unit = {},
        val durationMillis: Long,
        val snackbarState: SnackbarHostState
    ) : BookInfoEvent()

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

    data object OnShowMoreBottomSheet : BookInfoEvent()

    data object OnShowDetailsBottomSheet : BookInfoEvent()

    data object OnDismissBottomSheet : BookInfoEvent()

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

    data class OnCheckForTextUpdate(
        val snackbarState: SnackbarHostState,
        val context: Context
    ) : BookInfoEvent()

    data class OnShowUpdateDialog(
        val updatedText: List<String>,
        val updatedChapters: List<Chapter>
    ) : BookInfoEvent()

    data class OnActionUpdateDialog(
        val snackbarState: SnackbarHostState,
        val context: Context
    ) : BookInfoEvent()

    data object OnDismissUpdateDialog : BookInfoEvent()

    data object OnCancelTextUpdate : BookInfoEvent()
}