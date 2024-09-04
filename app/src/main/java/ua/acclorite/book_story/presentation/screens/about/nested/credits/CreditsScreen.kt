package ua.acclorite.book_story.presentation.screens.about.nested.credits

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.CustomLazyColumn
import ua.acclorite.book_story.presentation.core.components.GoBackButton
import ua.acclorite.book_story.presentation.core.components.LocalAboutViewModel
import ua.acclorite.book_story.presentation.core.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.core.components.customItems
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.about.data.AboutEvent
import ua.acclorite.book_story.presentation.screens.about.nested.credits.components.CreditItem

@Composable
fun CreditsScreenRoot() {
    CreditsScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreditsScreen() {
    val onEvent = LocalAboutViewModel.current.onEvent
    val onNavigate = LocalOnNavigate.current
    val context = LocalContext.current

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
                    Text(stringResource(id = R.string.credits_option))
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
        CustomLazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = scrollState.second
        ) {
            customItems(Constants.CREDITS, key = { it.name }) {
                CreditItem(credit = it) {
                    it.website?.let { website ->
                        onEvent(
                            AboutEvent.OnNavigateToBrowserPage(
                                page = website,
                                context = context,
                                noAppsFound = {
                                    context.getString(R.string.error_no_browser)
                                        .showToast(context = context, longToast = false)
                                }
                            )
                        )
                    }
                }
            }
        }
    }
}







