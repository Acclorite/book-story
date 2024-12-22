package ua.acclorite.book_story.presentation.settings.browse

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseSettingsContent(
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateBack: () -> Unit
) {
    BrowseSettingsScaffold(
        listState = listState,
        scrollBehavior = scrollBehavior,
        navigateBack = navigateBack
    )
}