package ua.acclorite.book_story.presentation.browse

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SdStorage
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.dialog.Dialog
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.ui.browse.BrowseEvent

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BrowsePermissionDialog(
    storagePermissionState: PermissionState,
    actionPermissionDialog: (BrowseEvent.OnActionPermissionDialog) -> Unit,
    dismissPermissionDialog: (BrowseEvent.OnDismissPermissionDialog) -> Unit
) {
    val activity = LocalActivity.current

    Dialog(
        title = stringResource(id = R.string.storage_permission),
        imageVectorIcon = Icons.Outlined.SdStorage,
        description = stringResource(id = R.string.storage_permission_description),
        actionText = stringResource(id = R.string.grant),
        actionEnabled = true,
        disableOnClick = false,
        onDismiss = {
            dismissPermissionDialog(
                BrowseEvent.OnDismissPermissionDialog(
                    storagePermissionState = storagePermissionState
                )
            )
        },
        onAction = {
            actionPermissionDialog(
                BrowseEvent.OnActionPermissionDialog(
                    activity = activity,
                    storagePermissionState = storagePermissionState
                )
            )
        },
        withDivider = false
    )
}