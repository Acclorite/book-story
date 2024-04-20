package ua.acclorite.book_story.presentation.screens.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpAddBooksItem
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpClickMeNoteItem
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpCustomizeApp
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpCustomizeReader
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpEditBook
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpFindBooksItem
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpManageHistory
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpMoveOrDeleteBooks
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpReadBook
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpUpdateBook
import ua.acclorite.book_story.presentation.screens.help.data.HelpEvent
import ua.acclorite.book_story.presentation.screens.help.data.HelpState
import ua.acclorite.book_story.presentation.screens.help.data.HelpViewModel
import ua.acclorite.book_story.presentation.screens.start.data.StartEvent
import ua.acclorite.book_story.presentation.screens.start.data.StartViewModel

@Composable
fun HelpScreenRoot() {
    val navigator = LocalNavigator.current
    val helpViewModel: HelpViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val startViewModel: StartViewModel = hiltViewModel()

    val state = helpViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        helpViewModel.init(navigator)
    }

    HelpScreen(
        state = state,
        navigator = navigator,
        onEvent = helpViewModel::onEvent,
        onMainEvent = mainViewModel::onEvent,
        onStartEvent = startViewModel::onEvent
    )
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun HelpScreen(
    state: State<HelpState>,
    navigator: Navigator,
    onEvent: (HelpEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    onStartEvent: (StartEvent) -> Unit
) {
    val scrollState = TopAppBarDefaults.collapsibleUntilExitScrollBehaviorWithLazyListState()

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollState.first.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(id = R.string.help_screen))
                },
                navigationIcon = {
                    if (!state.value.fromStart) {
                        GoBackButton(navigator = navigator)
                    }
                },
                actions = {
                    if (!state.value.fromStart) {
                        CustomIconButton(
                            icon = Icons.Default.RestartAlt,
                            contentDescription = R.string.reset_start_content_desc,
                            disableOnClick = false
                        ) {
                            onStartEvent(StartEvent.OnResetStartScreen)
                            onMainEvent(MainEvent.OnChangeShowStartScreen(true))
                            navigator.navigateWithoutBackStack(Screen.START, false)
                            navigator.clearBackStack()
                        }
                    }
                },
                scrollBehavior = scrollState.first,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        bottomBar = {
            if (state.value.fromStart) {
                Column {
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(bottom = 8.dp)
                            .padding(horizontal = 18.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(100),
                        onClick = {
                            navigator.clearArgument("from_start")
                            onMainEvent(MainEvent.OnChangeShowStartScreen(false))
                            navigator.navigateWithoutBackStack(Screen.BROWSE, false)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.done))
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
            state = scrollState.second,
            contentPadding = PaddingValues(horizontal = 18.dp)
        ) {
            item {
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(16.dp)
                )
            }

            item {
                HelpClickMeNoteItem(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(16.dp)
                )
            }

            item {
                HelpFindBooksItem(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                HelpAddBooksItem(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                HelpCustomizeApp(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                HelpMoveOrDeleteBooks(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                HelpEditBook(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                HelpReadBook(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                HelpCustomizeReader(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                HelpUpdateBook(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                HelpManageHistory(
                    state = state,
                    onEvent = onEvent
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(48.dp)
                )
            }
        }
    }
}