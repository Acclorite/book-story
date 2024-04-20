package ua.acclorite.book_story.presentation.screens.book_info.data

import android.content.Context
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.presentation.data.Navigator

@Immutable
sealed class BookInfoEvent {
    data object OnShowHideChangeCoverBottomSheet : BookInfoEvent()
    data object OnShowHideDetailsBottomSheet : BookInfoEvent()
    data class OnDeleteCover(val refreshList: (Book) -> Unit) : BookInfoEvent()
    data class OnChangeCover(val uri: Uri, val context: Context, val refreshList: (Book) -> Unit) :
        BookInfoEvent()

    data object OnShowHideEditTitle : BookInfoEvent()
    data class OnRequestFocus(val focusRequester: FocusRequester) : BookInfoEvent()
    data class OnTitleValueChange(val value: String) : BookInfoEvent()
    data class OnUpdateTitle(val refreshList: (Book) -> Unit) : BookInfoEvent()
    data object OnShowHideDeleteDialog : BookInfoEvent()
    data object OnShowHideMoveDialog : BookInfoEvent()
    data class OnDeleteBook(val refreshList: () -> Unit, val navigator: Navigator) : BookInfoEvent()
    data class OnMoveBook(
        val refreshList: (Book) -> Unit,
        val updatePage: (Int) -> Unit,
        val navigator: Navigator
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
        val navigator: Navigator
    ) : BookInfoEvent()

    data object OnDismissConfirmUpdateDialog : BookInfoEvent()

    data class OnShowConfirmUpdateDialog(
        val updatedBook: Book,
        val authorUpdated: Boolean,
        val descriptionUpdated: Boolean,
        val textUpdated: Boolean,
    ) : BookInfoEvent()

    data object OnCancelUpdate : BookInfoEvent()

    data class OnConfirmUpdate(
        val snackbarState: SnackbarHostState,
        val context: Context,
        val refreshList: (Book) -> Unit
    ) : BookInfoEvent()
}