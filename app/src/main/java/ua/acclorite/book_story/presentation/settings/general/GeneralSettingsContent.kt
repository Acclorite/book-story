package ua.acclorite.book_story.presentation.settings.general

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettingsContent(
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateBack: () -> Unit
) {
    GeneralSettingsScaffold(
        listState = listState,
        scrollBehavior = scrollBehavior,
        navigateBack = navigateBack
    )
}