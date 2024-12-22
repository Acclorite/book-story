package ua.acclorite.book_story.presentation.settings

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToGeneralSettings: () -> Unit,
    navigateToAppearanceSettings: () -> Unit,
    navigateToReaderSettings: () -> Unit,
    navigateToBrowseSettings: () -> Unit,
    navigateBack: () -> Unit
) {
    SettingsScaffold(
        listState = listState,
        scrollBehavior = scrollBehavior,
        navigateToGeneralSettings = navigateToGeneralSettings,
        navigateToAppearanceSettings = navigateToAppearanceSettings,
        navigateToReaderSettings = navigateToReaderSettings,
        navigateToBrowseSettings = navigateToBrowseSettings,
        navigateBack = navigateBack
    )
}