@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.presentation.screens.browse.data

import androidx.activity.ComponentActivity
import androidx.compose.ui.focus.FocusRequester
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.util.Selected
import java.io.File

sealed class BrowseEvent {
    data class OnStoragePermissionRequest(
        val activity: ComponentActivity, val hideErrorMessage: () -> Unit
    ) : BrowseEvent()

    data class OnLegacyStoragePermissionRequest(
        val permissionState: PermissionState, val hideErrorMessage: () -> Unit
    ) : BrowseEvent()

    data class OnStoragePermissionDismiss(
        val permissionState: PermissionState, val showErrorMessage: () -> Unit
    ) : BrowseEvent()

    data object OnRefreshList : BrowseEvent()
    data class OnPermissionCheck(
        val permissionState: PermissionState,
        val hideErrorMessage: () -> Unit
    ) : BrowseEvent()

    data class OnSelectFile(val file: Pair<File, Selected>) : BrowseEvent()
    data class OnSelectBook(val book: Pair<NullableBook, Selected>) : BrowseEvent()
    data object OnSearchShowHide : BrowseEvent()
    data class OnRequestFocus(val focusRequester: FocusRequester) : BrowseEvent()
    data object OnClearSelectedFiles : BrowseEvent()
    data class OnSearchQueryChange(val query: String) : BrowseEvent()
    data object OnAddingDialogRequest : BrowseEvent()
    data object OnAddingDialogDismiss : BrowseEvent()
    data object OnGetBooksFromFiles : BrowseEvent()
    data class OnAddBooks(val navigator: Navigator, val resetScroll: () -> Unit) : BrowseEvent()
    data object OnLoadList : BrowseEvent()

}