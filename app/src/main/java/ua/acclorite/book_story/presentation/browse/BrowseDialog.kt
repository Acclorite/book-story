package ua.acclorite.book_story.presentation.browse

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.domain.library.book.SelectableNullableBook
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.browse.BrowseEvent
import ua.acclorite.book_story.ui.browse.BrowseScreen


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BrowseDialog(
    dialog: Dialog?,
    storagePermissionState: PermissionState,
    loadingAddDialog: Boolean,
    selectedBooksAddDialog: List<SelectableNullableBook>,
    dismissAddDialog: (BrowseEvent.OnDismissAddDialog) -> Unit,
    actionAddDialog: (BrowseEvent.OnActionAddDialog) -> Unit,
    selectAddDialog: (BrowseEvent.OnSelectAddDialog) -> Unit,
    actionPermissionDialog: (BrowseEvent.OnActionPermissionDialog) -> Unit,
    dismissPermissionDialog: (BrowseEvent.OnDismissPermissionDialog) -> Unit,
    navigateToLibrary: () -> Unit
) {
    when (dialog) {
        BrowseScreen.PERMISSION_DIALOG -> {
            BrowsePermissionDialog(
                storagePermissionState = storagePermissionState,
                actionPermissionDialog = actionPermissionDialog,
                dismissPermissionDialog = dismissPermissionDialog
            )
        }

        BrowseScreen.ADD_DIALOG -> {
            BrowseAddDialog(
                loadingAddDialog = loadingAddDialog,
                selectedBooksAddDialog = selectedBooksAddDialog,
                dismissAddDialog = dismissAddDialog,
                actionAddDialog = actionAddDialog,
                selectAddDialog = selectAddDialog,
                navigateToLibrary = navigateToLibrary
            )
        }
    }
}