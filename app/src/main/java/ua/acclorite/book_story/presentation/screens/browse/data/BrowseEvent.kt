@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.presentation.screens.browse.data

import androidx.activity.ComponentActivity
import androidx.compose.ui.focus.FocusRequester
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.presentation.Navigator
import java.io.File

sealed class BrowseEvent {
    data class OnStoragePermissionRequest(val activity: ComponentActivity) : BrowseEvent()
    data class OnLegacyStoragePermissionRequest(val permissionState: PermissionState) :
        BrowseEvent()

    data class OnStoragePermissionDismiss(val permissionState: PermissionState) : BrowseEvent()
    data object OnRefreshList : BrowseEvent()
    data class OnPermissionCheck(val permissionState: PermissionState) : BrowseEvent()
    data class OnSelectFile(val file: Pair<File, Boolean>) : BrowseEvent()
    data class OnSelectBook(val book: NullableBook) : BrowseEvent()
    data object OnSearchShowHide : BrowseEvent()
    data class OnRequestFocus(val focusRequester: FocusRequester) : BrowseEvent()
    data object OnClearSelectedFiles : BrowseEvent()
    data class OnSearchQueryChange(val query: String) : BrowseEvent()
    data object OnAddingDialogRequest : BrowseEvent()
    data object OnAddingDialogDismiss : BrowseEvent()
    data object OnGetBooksFromFiles : BrowseEvent()
    data class OnAddBooks(val navigator: Navigator, val resetScroll: () -> Unit) : BrowseEvent()
    data object OnLoadList : BrowseEvent()
    data class OnUpdateScrollIndex(val index: Int) : BrowseEvent()
    data class OnUpdateScrollOffset(val offset: Int) : BrowseEvent()


}