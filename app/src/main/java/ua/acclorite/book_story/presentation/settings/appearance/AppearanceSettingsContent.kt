package ua.acclorite.book_story.presentation.settings.appearance

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettingsContent(
    scrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
    navigateBack: () -> Unit
) {
    AppearanceSettingsScaffold(
        scrollBehavior = scrollBehavior,
        listState = listState,
        navigateBack = navigateBack
    )
}