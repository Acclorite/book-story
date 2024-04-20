package ua.acclorite.book_story.presentation.screens.browse.components

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent

/**
 * Storage permission dialog.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BrowseStoragePermissionDialog(
    onEvent: (BrowseEvent) -> Unit,
    permissionState: PermissionState,
    showErrorMessage: (Boolean) -> Unit
) {
    val activity = LocalContext.current as ComponentActivity

    CustomDialogWithContent(
        title = stringResource(id = R.string.storage_permission),
        description = stringResource(id = R.string.storage_permission_description),
        actionText = stringResource(id = R.string.grant),
        imageVectorIcon = Icons.Default.SdStorage,
        onDismiss = {
            onEvent(
                BrowseEvent.OnStoragePermissionDismiss(
                    permissionState,
                    showErrorMessage = { showErrorMessage(true) }
                )
            )
        },
        isActionEnabled = true,
        withDivider = false,
        disableOnClick = false,
        onAction = {
            onEvent(
                BrowseEvent.OnStoragePermissionRequest(
                    activity,
                    storagePermissionState = permissionState,
                    hideErrorMessage = { showErrorMessage(false) }
                )
            )
        }
    )
}