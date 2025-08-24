/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.browse.scan.components

import android.content.Context
import android.content.UriPermission
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.getBasePath
import com.anggrayudi.storage.file.getRootPath
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.browse.BrowseScreen
import ua.acclorite.book_story.presentation.settings.SettingsEvent
import ua.acclorite.book_story.presentation.settings.SettingsModel
import ua.acclorite.book_story.ui.common.components.common.IconButton
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.helpers.noRippleClickable
import ua.acclorite.book_story.ui.common.helpers.showToast
import ua.acclorite.book_story.ui.theme.dynamicListItemColor

@Composable
fun BrowseScanOption() {
    val settingsModel = hiltViewModel<SettingsModel>()
    val context = LocalContext.current

    fun getPersistedUriPermissions(): List<UriPermission> {
        return context.contentResolver?.persistedUriPermissions.let { permissions ->
            if (permissions.isNullOrEmpty()) return@let emptyList()
            permissions.sortedBy { it.uri.path?.lowercase() }
        }
    }

    val persistedUriPermissions = remember {
        mutableStateListOf<UriPermission>().apply {
            clear()
            addAll(getPersistedUriPermissions())
        }
    }

    val persistedUriIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        settingsModel.onEvent(
            SettingsEvent.OnGrantPersistableUriPermission(
                uri = uri.toString()
            )
        )

        persistedUriPermissions.clear()
        persistedUriPermissions.addAll(getPersistedUriPermissions())
        BrowseScreen.refreshListChannel.trySend(Unit)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        persistedUriPermissions.forEachIndexed { index, permission ->
            BrowseScanFolderItem(
                index = index,
                permission = permission,
                context = context,
                releasePersistableUriPermission = {
                    settingsModel.onEvent(
                        SettingsEvent.OnReleasePersistableUriPermission(
                            uri = permission.uri.toString()
                        )
                    )

                    persistedUriPermissions.clear()
                    persistedUriPermissions.addAll(getPersistedUriPermissions())
                    BrowseScreen.refreshListChannel.trySend(Unit)
                }
            )
        }
    }

    BrowseScanAction(
        requestPersistableUriPermission = {
            try {
                persistedUriIntent.launch(null)
            } catch (e: Exception) {
                e.printStackTrace()

                context.getString(R.string.error_no_file_manager_app)
                    .showToast(context, longToast = false)
            }
        }
    )
}

@Composable
private fun BrowseScanFolderItem(
    index: Int,
    permission: UriPermission,
    context: Context,
    releasePersistableUriPermission: () -> Unit
) {
    val permissionFile = DocumentFileCompat.fromUri(context, permission.uri) ?: return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Folder,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.dynamicListItemColor(index))
                .padding(11.dp)
                .size(22.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            StyledText(
                text = permissionFile.getBasePath(context),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            StyledText(
                text = permissionFile.getRootPath(context),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        IconButton(
            modifier = Modifier.size(24.dp),
            icon = Icons.Outlined.Clear,
            contentDescription = R.string.remove_content_desc,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.onSurface
        ) {
            releasePersistableUriPermission()
        }
    }
}

@Composable
private fun BrowseScanAction(
    requestPersistableUriPermission: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 18.dp)
            .noRippleClickable {
                requestPersistableUriPermission()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
        StyledText(
            text = stringResource(id = R.string.add_folder),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }
}