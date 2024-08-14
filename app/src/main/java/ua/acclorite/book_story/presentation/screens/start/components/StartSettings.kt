package ua.acclorite.book_story.presentation.screens.start.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.start.components.permissions.startPermissionsScreen
import ua.acclorite.book_story.presentation.screens.start.data.StartEvent
import ua.acclorite.book_story.presentation.screens.start.data.StartNavigationScreen
import ua.acclorite.book_story.presentation.screens.start.data.StartState

/**
 * Start Settings.
 *
 * @param state [StartState].
 * @param mainState [MainState].
 * @param onEvent [StartEvent] callback.
 * @param onMainEvent [MainEvent] callback.
 * @param storagePermissionState Storage [PermissionState].
 * @param notificationsPermissionState Notifications [PermissionState].
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StartSettings(
    state: State<StartState>,
    mainState: State<MainState>,
    onEvent: (StartEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    storagePermissionState: PermissionState,
    notificationsPermissionState: PermissionState
) {
    val activity = LocalContext.current as ComponentActivity

    val languages = remember(mainState.value.language) {
        Constants.LANGUAGES.sortedBy { it.second }.map {
            ButtonItem(
                id = it.first,
                title = it.second,
                textStyle = TextStyle(),
                selected = it.first == mainState.value.language
            )
        }.sortedBy { it.title }
    }

    StartNavigationTransition(
        modifier = Modifier.padding(horizontal = 18.dp),
        state = state,
        visible = !state.value.isDone,
        bottomBar = {
            Column {
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = 8.dp)
                        .padding(horizontal = 18.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(100),
                    onClick = { onEvent(StartEvent.OnGoForward) },
                    enabled = state.value.storagePermissionGranted ||
                            state.value.currentScreen != StartNavigationScreen.PERMISSIONS_THIRD
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Icon(
            imageVector = Icons.Default.GolfCourse,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.start_welcome),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.start_welcome_desc),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    MaterialTheme.shapes.large
                ),
        ) {
            StartNavigationScreenItem(
                state = state,
                screen = StartNavigationScreen.LANGUAGE_FIRST
            ) {
                startLanguageScreen(onMainEvent = onMainEvent, languages = languages)
            }

            StartNavigationScreenItem(
                state = state,
                screen = StartNavigationScreen.APPEARANCE_SECOND
            ) {
                startAppearanceScreen(
                    mainState = mainState,
                    onMainEvent = onMainEvent
                )
            }

            StartNavigationScreenItem(
                state = state,
                screen = StartNavigationScreen.PERMISSIONS_THIRD
            ) {
                startPermissionsScreen(
                    state = state,
                    onGrantStoragePermission = {
                        onEvent(
                            StartEvent.OnStoragePermissionRequest(
                                activity,
                                storagePermissionState
                            )
                        )
                    },
                    onGrantNotificationsPermission = {
                        onEvent(
                            StartEvent.OnNotificationsPermissionRequest(
                                activity,
                                notificationsPermissionState
                            )
                        )
                    }
                )
            }
        }
    }
}