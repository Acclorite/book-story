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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.CustomLazyColumn
import ua.acclorite.book_story.presentation.core.components.GoBackButton
import ua.acclorite.book_story.presentation.core.components.LocalLicensesViewModel
import ua.acclorite.book_story.presentation.core.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.core.components.customItems
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.screens.about.nested.licenses.components.LicenseItem

@Composable
fun LicensesScreenRoot() {
    val viewModel = LocalLicensesViewModel.current.viewModel
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.init(context)
    }

    LicensesScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicensesScreen() {
    val state = LocalLicensesViewModel.current.state
    val onNavigate = LocalOnNavigate.current

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
            CustomLazyColumn(
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