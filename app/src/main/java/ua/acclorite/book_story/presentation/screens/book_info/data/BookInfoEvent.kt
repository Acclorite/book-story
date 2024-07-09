package ua.acclorite.book_story.presentation.screens.book_info.data

import android.content.Context
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.domain.util.UIText

@Immutable
sealed class BookInfoEvent {
    data object OnShowHideChangeCoverBottomSheet : BookInfoEvent()
    data object OnShowHideDetailsBottomSheet : BookInfoEvent()
    data class OnDeleteCover(val refreshList: (Book) -> Unit) : BookInfoEvent()
    data class OnChangeCover(val uri: Uri, val context: Context, val refreshList: (Book) -> Unit) :
        BookInfoEvent()

    data object OnCheckCoverReset : BookInfoEvent()
    data class OnResetCoverImage(
        val refreshList: (Book) -> Unit,
        val showResult: (UIText) -> Unit
    ) : BookInfoEvent()

    data object OnShowHideEditTitle : BookInfoEvent()
    data class OnTitleRequestFocus(val focusRequester: FocusRequester) : BookInfoEvent()
    data class OnTitleValueChange(val value: String) : BookInfoEvent()

    data object OnShowHideEditAuthor : BookInfoEvent()
    data class OnAuthorRequestFocus(val focusRequester: FocusRequester) : BookInfoEvent()
    data class OnAuthorValueChange(val value: String) : BookInfoEvent()

    data object OnShowHideEditDescription : BookInfoEvent()
    data class OnDescriptionRequestFocus(val focusRequester: FocusRequester) : BookInfoEvent()
    data class OnDescriptionValueChange(val value: String) : BookInfoEvent()

    data class OnUpdateData(val refreshList: (Book) -> Unit) : BookInfoEvent()

    data object OnShowHideDeleteDialog : BookInfoEvent()
    data object OnShowHideMoveDialog : BookInfoEvent()
    data class OnDeleteBook(val refreshList: () -> Unit, val onNavigate: OnNavigate) :
        BookInfoEvent()

    data class OnMoveBook(
        val refreshList: (Book) -> Unit,
        val updatePage: (Int) -> Unit,
        val onNavigate: OnNavigate
    ) : BookInfoEvent()

    data class OnSelectCategory(val category: Category) : BookInfoEvent()
    data class OnCopyToClipboard(
        val context: Context,
        val text: String,
        val success: () -> Unit
    ) : BookInfoEvent()

    data class OnShowSnackbar(
        val text: String,
        val action: String?,
        val onAction: () -> Unit = {},
        val durationMillis: Long,
        val snackbarState: SnackbarHostState
    ) : BookInfoEvent()

    data class OnLoadUpdate(
        val snackbarState: SnackbarHostState,
        val context: Context
    ) : BookInfoEvent()

    data class OnNavigateToReaderScreen(
        val onNavigate: OnNavigate
    ) : BookInfoEvent()

    data object OnDismissConfirmUpdateDialog : BookInfoEvent()

    data class OnShowConfirmUpdateDialog(
        val updatedText: List<String>
    ) : BookInfoEvent()

    data object OnCancelUpdate : BookInfoEvent()

    data class OnConfirmUpdate(
        val snackbarState: SnackbarHostState,
        val context: Context,
        val refreshList: (Book) -> Unit
    ) : BookInfoEvent()
}