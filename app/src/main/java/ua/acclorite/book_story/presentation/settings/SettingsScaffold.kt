package ua.acclorite.book_story.presentation.settings

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScaffold(
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToGeneralSettings: () -> Unit,
    navigateToAppearanceSettings: () -> Unit,
    navigateToReaderSettings: () -> Unit,
    navigateToBrowseSettings: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            SettingsTopBar(
                scrollBehavior = scrollBehavior,
                navigateBack = navigateBack
            )
        }
    ) { paddingValues ->
        SettingsLayout(
            listState = listState,
            paddingValues = paddingValues,
            navigateToGeneralSettings = navigateToGeneralSettings,
            navigateToAppearanceSettings = navigateToAppearanceSettings,
            navigateToReaderSettings = navigateToReaderSettings,
            navigateToBrowseSettings = navigateToBrowseSettings
        )
    }
}