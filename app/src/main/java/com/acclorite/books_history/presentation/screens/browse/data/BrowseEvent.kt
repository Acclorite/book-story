@file:OptIn(ExperimentalPermissionsApi::class)

package com.acclorite.books_history.presentation.screens.browse.data

import androidx.activity.ComponentActivity
import androidx.compose.ui.focus.FocusRequester
import com.acclorite.books_history.domain.model.NullableBook
import com.acclorite.books_history.presentation.Navigator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import java.io.File

sealed class BrowseEvent() {
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
    data class OnAddBooks(val navigator: Navigator) : BrowseEvent()
    data object OnLoadList : BrowseEvent()
}