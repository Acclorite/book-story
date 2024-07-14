package ua.acclorite.book_story.presentation.screens.about.nested.licenses

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.about.nested.licenses.components.LicenseItem
import ua.acclorite.book_story.presentation.screens.about.nested.licenses.data.LicensesState
import ua.acclorite.book_story.presentation.screens.about.nested.licenses.data.LicensesViewModel

@Composable
fun LicensesScreenRoot() {
    val navigator = LocalNavigator.current
    val context = LocalContext.current
    val licensesViewModel: LicensesViewModel = hiltViewModel()

    val state = licensesViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        licensesViewModel.init(context)
    }

    LicensesScreen(
        state = state,
        onNavigate = { navigator.it() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicensesScreen(
    state: State<LicensesState>,
    onNavigate: OnNavigate
) {
    val scrollState = TopAppBarDefaults.collapsibleUntilExitScrollBehaviorWithLazyListState(
        state.value.listState
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
                    Text(stringResource(id = R.string.licenses_option))
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
        CustomAnimatedVisibility(
            visible = state.value.licenses.isNotEmpty(),
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                state = scrollState.second
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                customItems(state.value.licenses, key = { it.uniqueId }) {
                    LicenseItem(library = it) {
                        onNavigate {
                            navigate(Screen.About.LicenseInfo(it.uniqueId))
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}