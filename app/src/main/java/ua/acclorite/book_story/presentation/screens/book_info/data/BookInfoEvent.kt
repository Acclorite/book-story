package ua.acclorite.book_story.presentation.screens.book_info.data

import android.content.Context
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.presentation.data.Navigator

sealed class BookInfoEvent {
    data object OnShowHideChangeCoverBottomSheet : BookInfoEvent()
    data object OnShowHideDetailsBottomSheet : BookInfoEvent()
    data class OnDeleteCover(val refreshList: () -> Unit) : BookInfoEvent()
    data class OnChangeCover(val uri: Uri, val context: Context, val refreshList: () -> Unit) :
        BookInfoEvent()

    data object OnShowHideEditTitle : BookInfoEvent()
    data class OnRequestFocus(val focusRequester: FocusRequester) : BookInfoEvent()
    data class OnTitleValueChange(val value: String) : BookInfoEvent()
    data class OnUpdateTitle(val refreshList: () -> Unit) : BookInfoEvent()
    data object OnShowHideMoreDropDown : BookInfoEvent()
    data object OnShowHideDeleteDialog : BookInfoEvent()
    data object OnShowHideMoveDialog : BookInfoEvent()
    data class OnDeleteBook(val refreshList: () -> Unit, val navigator: Navigator) : BookInfoEvent()
    data class OnMoveBook(
        val refreshList: () -> Unit,
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

    data class OnUpdateBook(
        val refreshList: () -> Unit,
        val snackbarState: SnackbarHostState,
        val context: Context
    ) : BookInfoEvent()
}