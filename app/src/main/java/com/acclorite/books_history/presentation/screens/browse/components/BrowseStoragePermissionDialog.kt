package com.acclorite.books_history.presentation.screens.browse.components

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.components.CustomAlertDialog
import com.acclorite.books_history.presentation.screens.browse.data.BrowseEvent
import com.acclorite.books_history.presentation.screens.browse.data.BrowseViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BrowseStoragePermissionDialog(viewModel: BrowseViewModel, permissionState: PermissionState) {
    val activity = LocalContext.current as ComponentActivity

    CustomAlertDialog(
        title = stringResource(id = R.string.storage_permission),
        description = stringResource(id = R.string.storage_permission_description),
        actionText = stringResource(id = R.string.grant),
        imageVectorIcon = Icons.Default.SdStorage,
        onDismiss = { viewModel.onEvent(BrowseEvent.OnStoragePermissionDismiss(permissionState)) },
        onAction = {
            viewModel.onEvent(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    BrowseEvent.OnStoragePermissionRequest(activity)
                } else {
                    BrowseEvent.OnLegacyStoragePermissionRequest(permissionState)
                }
            )
        }
    )
}