@file:OptIn(ExperimentalPermissionsApi::class)

package ua.acclorite.book_story.presentation.screens.start

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.start.components.StartDone
import ua.acclorite.book_story.presentation.screens.start.components.StartSettings
import ua.acclorite.book_story.presentation.screens.start.data.StartEvent
import ua.acclorite.book_story.presentation.screens.start.data.StartState
import ua.acclorite.book_story.presentation.screens.start.data.StartViewModel

@SuppressLint("InlinedApi")
@Composable
fun StartScreenRoot() {
    val startViewModel: StartViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val browseViewModel: BrowseViewModel = hiltViewModel()

    val state = startViewModel.state.collectAsState()
    val mainState = mainViewModel.state.collectAsState()
    val storagePermissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val notificationsPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )

    LaunchedEffect(Unit) {
        startViewModel.checkPermissions(
            storagePermissionState = storagePermissionState,
            notificationPermissionState = notificationsPermissionState
        )
    }

    StartScreen(
        state = state,
        mainState = mainState,
        storagePermissionState = storagePermissionState,
        notificationsPermissionState = notificationsPermissionState,
        onEvent = startViewModel::onEvent,
        onMainEvent = mainViewModel::onEvent,
        onBrowseEvent = browseViewModel::onEvent
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "InlinedApi")
@Composable
private fun StartScreen(
    state: State<StartState>,
    mainState: State<MainState>,
    storagePermissionState: PermissionState,
    notificationsPermissionState: PermissionState,
    onEvent: (StartEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    onBrowseEvent: (BrowseEvent) -> Unit,
) {
    val activity = LocalContext.current as ComponentActivity

    // Settings
    StartSettings(
        state = state,
        mainState = mainState,
        onEvent = onEvent,
        onMainEvent = onMainEvent,
        storagePermissionState = storagePermissionState,
        notificationsPermissionState = notificationsPermissionState
    )

    // Done
    StartDone(
        state = state,
        onEvent = onEvent,
        onMainEvent = onMainEvent,
        onBrowseEvent = onBrowseEvent
    )

    BackHandler {
        onEvent(
            StartEvent.OnGoBack {
                activity.finish()
            }
        )
    }
}