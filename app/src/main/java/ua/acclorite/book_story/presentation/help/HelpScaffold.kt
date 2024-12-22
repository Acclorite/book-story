package ua.acclorite.book_story.presentation.help

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import ua.acclorite.book_story.ui.main.MainEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScaffold(
    fromStart: Boolean,
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    changeShowStartScreen: (MainEvent.OnChangeShowStartScreen) -> Unit,
    navigateToBrowse: () -> Unit,
    navigateToStart: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            HelpTopBar(
                fromStart = fromStart,
                scrollBehavior = scrollBehavior,
                navigateToStart = navigateToStart,
                navigateBack = navigateBack
            )
        },
        bottomBar = {
            if (fromStart) HelpBottomBar(
                navigateToBrowse = navigateToBrowse,
                changeShowStartScreen = changeShowStartScreen
            )
        }
    ) { paddingValues ->
        HelpLayout(
            paddingValues = paddingValues,
            listState = listState
        )
    }
}