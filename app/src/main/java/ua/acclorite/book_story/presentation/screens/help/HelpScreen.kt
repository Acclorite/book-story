package ua.acclorite.book_story.presentation.screens.help

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpAddBooksItem
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpClickMeNoteItem
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpCustomizeApp
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpCustomizeReader
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpEditBook
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpFindBooksItem
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpMoveOrDeleteBooks
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpReadBook
import ua.acclorite.book_story.presentation.screens.help.components.items.HelpUpdateBook
import ua.acclorite.book_story.presentation.screens.help.data.HelpViewModel

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun HelpScreen(
    viewModel: HelpViewModel = hiltViewModel(),
    navigator: Navigator
) {
    val scrollState = TopAppBarDefaults.collapsibleScrollBehaviorWithLazyListState()

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
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding(), start = 18.dp, end = 18.dp)
                .imePadding(),
            state = scrollState.second
        ) {
            item {
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(16.dp)
                )
            }

            item {
                HelpClickMeNoteItem(viewModel = viewModel)
            }

            item {
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(16.dp)
                )
            }


            item {
                HelpFindBooksItem(viewModel = viewModel)
            }

            item {
                HelpAddBooksItem(navigator = navigator, viewModel = viewModel)
            }

            item {
                HelpCustomizeApp(
                    navigator = navigator,
                    viewModel = viewModel
                )
            }

            item {
                HelpMoveOrDeleteBooks(
                    navigator = navigator,
                    viewModel = viewModel
                )
            }

            item {
                HelpEditBook(
                    navigator = navigator,
                    viewModel = viewModel
                )
            }

            item {
                HelpReadBook(
                    navigator = navigator,
                    viewModel = viewModel
                )
            }

            item {
                HelpCustomizeReader(
                    navigator = navigator,
                    viewModel = viewModel
                )
            }

            item {
                HelpUpdateBook(
                    navigator = navigator,
                    viewModel = viewModel
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