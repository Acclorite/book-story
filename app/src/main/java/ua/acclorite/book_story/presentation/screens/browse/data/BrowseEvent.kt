@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.presentation.screens.browse.data

import androidx.activity.ComponentActivity
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.domain.util.Selected
import java.io.File

@Immutable
sealed class BrowseEvent {
    data class OnStoragePermissionRequest(
        val activity: ComponentActivity,
        val storagePermissionState: PermissionState
    ) : BrowseEvent()

    data class OnStoragePermissionDismiss(
        val permissionState: PermissionState
    ) : BrowseEvent()

    data object OnRefreshList : BrowseEvent()
    data class OnPermissionCheck(
        val permissionState: PermissionState
    ) : BrowseEvent()

    data class OnSelectFile(
        val includedFileFormats: List<String>,
        val file: SelectableFile
    ) : BrowseEvent()

    data class OnSelectFiles(
        val includedFileFormats: List<String>,
        val files: List<SelectableFile>
    ) : BrowseEvent()

    data class OnSelectBook(val book: Pair<NullableBook, Selected>) : BrowseEvent()
    data object OnSearchShowHide : BrowseEvent()
    data class OnRequestFocus(val focusRequester: FocusRequester) : BrowseEvent()
    data object OnClearSelectedFiles : BrowseEvent()
    data class OnSearchQueryChange(val query: String) : BrowseEvent()
    data object OnSearch : BrowseEvent()
    data object OnAddingDialogRequest : BrowseEvent()
    data object OnAddingDialogDismiss : BrowseEvent()
    data object OnGetBooksFromFiles : BrowseEvent()
    data class OnAddBooks(
        val onNavigate: OnNavigate,
        val resetScroll: () -> Unit,
        val onFailed: () -> Unit,
        val onSuccess: () -> Unit
    ) : BrowseEvent()

    data object OnLoadList : BrowseEvent()
    data object OnUpdateScrollOffset : BrowseEvent()
    data class OnChangeDirectory(val directory: File, val savePreviousDirectory: Boolean) :
        BrowseEvent()

    data object OnGoBackDirectory : BrowseEvent()
    data object OnShowHideFilterBottomSheet : BrowseEvent()
    data class OnScrollToFilterPage(val page: Int, val pagerState: PagerState?) : BrowseEvent()
    data class OnUpdateFavoriteDirectory(val path: String) : BrowseEvent()
}