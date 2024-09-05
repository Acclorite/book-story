package ua.acclorite.book_story.presentation.screens.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.CustomIconButton
import ua.acclorite.book_story.presentation.core.components.CustomLazyColumn
import ua.acclorite.book_story.presentation.core.components.GoBackButton
import ua.acclorite.book_story.presentation.core.components.LocalBrowseViewModel
import ua.acclorite.book_story.presentation.core.components.LocalHelpViewModel
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.core.components.LocalStartViewModel
import ua.acclorite.book_story.presentation.core.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.core.components.customItems
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.help.components.HelpItem
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpClickMeNoteItem
import ua.acclorite.book_story.presentation.screens.start.data.StartEvent

@Composable
fun HelpScreenRoot(screen: Screen.Help) {
    val viewModel = LocalHelpViewModel.current.viewModel

    LaunchedEffect(Unit) {
        viewModel.init(screen = screen)
    }

    HelpScreen()
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun HelpScreen() {
    val state = LocalHelpViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent
    val onBrowseEvent = LocalBrowseViewModel.current.onEvent
    val onStartEvent = LocalStartViewModel.current.onEvent
    val onNavigate = LocalOnNavigate.current

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
                        GoBackButton(onNavigate = onNavigate)
                    }
                },
                actions = {
                    if (!state.value.fromStart) {
                        CustomIconButton(
                            icon = Icons.Outlined.RestartAlt,
                            contentDescription = R.string.reset_start_content_desc,
                            disableOnClick = false
                        ) {
                            onStartEvent(StartEvent.OnResetStartScreen)
                            onMainEvent(MainEvent.OnChangeShowStartScreen(true))
                            onNavigate {
                                navigate(Screen.Start, saveInBackStack = false)
                                clearBackStack()
                            }
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
                            onBrowseEvent(BrowseEvent.OnLoadList)
                            onMainEvent(MainEvent.OnChangeShowStartScreen(false))
                            onNavigate {
                                navigate(Screen.Browse, saveInBackStack = false)
                            }
                        }
                    ) {
                        Text(text = stringResource(id = R.string.done))
                    }
                }
            }
        }
    ) { paddingValues ->
        CustomLazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = scrollState.second
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                HelpClickMeNoteItem()
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            customItems(Constants.HELP_TIPS, key = { it.title }) { helpTip ->
                HelpItem(
                    helpTip = helpTip,
                    fromStart = state.value.fromStart
                )
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}