package ua.acclorite.book_story.presentation.screens.start.components.permissions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.screens.start.data.StartState

/**
 * Permissions settings.
 */
fun LazyListScope.startPermissionsScreen(
    state: State<StartState>,
    onGrantStoragePermission: () -> Unit,
    onGrantNotificationsPermission: () -> Unit
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
    }
    item {
        CategoryTitle(
            modifier = Modifier,
            title = stringResource(id = R.string.start_permissions_preferences),
            color = MaterialTheme.colorScheme.primary
        )
    }
    item {
        Spacer(modifier = Modifier.height(8.dp))
    }

    item {
        StartPermissionItem(
            title = stringResource(id = R.string.start_permissions_storage),
            description = stringResource(id = R.string.start_permissions_storage_desc),
            isOptional = false,
            isGranted = state.value.storagePermissionGranted,
            onGrantClick = onGrantStoragePermission
        )
    }

    item {
        StartPermissionItem(
            title = stringResource(id = R.string.start_permissions_notifications),
            description = stringResource(id = R.string.start_permissions_notifications_desc),
            isOptional = true,
            isGranted = state.value.notificationsPermissionGranted,
            onGrantClick = onGrantNotificationsPermission
        )
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
    }
}