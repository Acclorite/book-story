package ua.acclorite.book_story.presentation.screens.about.nested.licenses

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.data.Argument
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Navigator
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
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicensesScreen(
    state: State<LicensesState>,
    navigator: Navigator
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
                    Text(stringResource(id = R.string.licenses_option))
                },
                navigationIcon = {
                    GoBackButton(navigator = navigator)
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
                state = scrollState.second,
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(state.value.licenses, key = { it.uniqueId }) {
                    LicenseItem(library = it) {
                        navigator.navigate(
                            Screen.LICENSES_INFO,
                            false,
                            Argument("license", it.uniqueId)
                        )
                    }
                }
            }
        }
    }
}