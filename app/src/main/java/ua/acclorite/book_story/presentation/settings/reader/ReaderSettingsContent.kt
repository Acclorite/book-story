package ua.acclorite.book_story.presentation.settings.reader

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsContent(
    scrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
    navigateBack: () -> Unit
) {
    ReaderSettingsScaffold(
        scrollBehavior = scrollBehavior,
        listState = listState,
        navigateBack = navigateBack
    )
}