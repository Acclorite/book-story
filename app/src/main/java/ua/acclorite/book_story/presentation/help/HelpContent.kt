package ua.acclorite.book_story.presentation.help

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.ui.main.MainEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpContent(
    fromStart: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
    changeShowStartScreen: (MainEvent.OnChangeShowStartScreen) -> Unit,
    navigateToBrowse: () -> Unit,
    navigateToStart: () -> Unit,
    navigateBack: () -> Unit
) {
    HelpScaffold(
        fromStart = fromStart,
        scrollBehavior = scrollBehavior,
        listState = listState,
        changeShowStartScreen = changeShowStartScreen,
        navigateToBrowse = navigateToBrowse,
        navigateToStart = navigateToStart,
        navigateBack = navigateBack
    )
}