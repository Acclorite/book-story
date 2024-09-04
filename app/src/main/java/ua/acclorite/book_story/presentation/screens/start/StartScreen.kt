@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.presentation.screens.start

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import ua.acclorite.book_story.presentation.core.components.LocalStartViewModel
import ua.acclorite.book_story.presentation.screens.start.components.StartDone
import ua.acclorite.book_story.presentation.screens.start.components.StartSettings
import ua.acclorite.book_story.presentation.screens.start.data.StartEvent

@SuppressLint("InlinedApi")
@Composable
fun StartScreenRoot() {
    val viewModel = LocalStartViewModel.current.viewModel

    val storagePermissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val notificationsPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )

    LaunchedEffect(Unit) {
        viewModel.checkPermissions(
            storagePermissionState = storagePermissionState,
            notificationPermissionState = notificationsPermissionState
        )
    }

    StartScreen(
        storagePermissionState = storagePermissionState,
        notificationsPermissionState = notificationsPermissionState,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "InlinedApi")
@Composable
private fun StartScreen(
    storagePermissionState: PermissionState,
    notificationsPermissionState: PermissionState
) {
    val onEvent = LocalStartViewModel.current.onEvent
    val activity = LocalContext.current as ComponentActivity

    // Settings
    StartSettings(
        storagePermissionState = storagePermissionState,
        notificationsPermissionState = notificationsPermissionState
    )

    // Done
    StartDone()

    BackHandler {
        onEvent(
            StartEvent.OnGoBack {
                activity.finish()
            }
        )
    }
}