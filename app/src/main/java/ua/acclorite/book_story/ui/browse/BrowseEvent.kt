@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.ui.browse

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.domain.library.book.SelectableNullableBook
import java.io.File

@Immutable
sealed class BrowseEvent {
    data class OnRefreshList(
        val showIndicator: Boolean,
        val hideSearch: Boolean
    ) : BrowseEvent()

    data class OnSearchVisibility(
        val show: Boolean
    ) : BrowseEvent()

    data class OnRequestFocus(
        val focusRequester: FocusRequester
    ) : BrowseEvent()

    data class OnSearchQueryChange(
        val query: String
    ) : BrowseEvent()

    data object OnSearch : BrowseEvent()

    data object OnClearSelectedFiles : BrowseEvent()

    data class OnChangeDirectory(
        val directory: File,
        val savePreviousDirectory: Boolean
    ) : BrowseEvent()

    data object OnGoBackDirectory : BrowseEvent()

    data class OnSelectFiles(
        val includedFileFormats: List<String>,
        val files: List<SelectableFile>
    ) : BrowseEvent()

    data class OnSelectFile(
        val includedFileFormats: List<String>,
        val file: SelectableFile
    ) : BrowseEvent()

    data class OnUpdateFavoriteDirectory(
        val path: String
    ) : BrowseEvent()

    data object OnShowFilterBottomSheet : BrowseEvent()

    data object OnDismissBottomSheet : BrowseEvent()

    data class OnPermissionCheck(
        val storagePermissionState: PermissionState
    ) : BrowseEvent()

    data class OnActionPermissionDialog(
        val activity: ComponentActivity,
        val storagePermissionState: PermissionState
    ) : BrowseEvent()

    data class OnDismissPermissionDialog(
        val storagePermissionState: PermissionState
    ) : BrowseEvent()

    data object OnShowAddDialog : BrowseEvent()

    data object OnDismissAddDialog : BrowseEvent()

    data class OnSelectAddDialog(
        val book: SelectableNullableBook
    ) : BrowseEvent()

    data class OnActionAddDialog(
        val context: Context,
        val navigateToLibrary: () -> Unit
    ) : BrowseEvent()

    data object OnDismissDialog : BrowseEvent()
}