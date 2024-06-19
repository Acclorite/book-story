package ua.acclorite.book_story.presentation.screens.settings.nested.general

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.screens.settings.components.ChipsWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsEvent
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsViewModel

@Composable
fun GeneralSettingsRoot() {
    val navigator = LocalNavigator.current
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()

    val state = mainViewModel.state.collectAsState()

    GeneralSettings(
        state = state,
        onNavigate = { navigator.it() },
        onSettingsEvent = settingsViewModel::onEvent,
        onMainEvent = mainViewModel::onEvent
    )
}

@SuppressLint("InlinedApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun GeneralSettings(
    state: State<MainState>,
    onNavigate: OnNavigate,
    onSettingsEvent: (SettingsEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit
) {
    val scrollState = TopAppBarDefaults.collapsibleUntilExitScrollBehaviorWithLazyListState()
    val activity = LocalContext.current as ComponentActivity

    val notificationsPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollState.first.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(id = R.string.general_settings))
                },
                navigationIcon = {
                    GoBackButton(onNavigate = onNavigate)
                },
                scrollBehavior = scrollState.first,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = scrollState.second
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                ChipsWithTitle(
                    title = stringResource(id = R.string.language_option),
                    chips = Constants.LANGUAGES.sortedBy { it.second }.map {
                        ButtonItem(
                            it.first,
                            it.second,
                            MaterialTheme.typography.labelLarge,
                            it.first == state.value.language
                        )
                    }.sortedBy { it.title }
                ) {
                    onMainEvent(
                        MainEvent.OnChangeLanguage(
                            it.id
                        )
                    )
                }
            }

            item {
                SwitchWithTitle(
                    selected = state.value.checkForUpdates!!,
                    title = stringResource(id = R.string.check_for_updates_option),
                    description = stringResource(id = R.string.check_for_updates_option_desc)
                ) {
                    onSettingsEvent(
                        SettingsEvent.OnGeneralChangeCheckForUpdates(
                            enable = !state.value.checkForUpdates!!,
                            activity = activity,
                            notificationsPermissionState = notificationsPermissionState,
                            onChangeCheckForUpdates = {
                                onMainEvent(
                                    MainEvent.OnChangeCheckForUpdates(
                                        it
                                    )
                                )
                            }
                        )
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}