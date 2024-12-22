package ua.acclorite.book_story.presentation.license_info

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
import com.mikepenz.aboutlibraries.entity.Library
import ua.acclorite.book_story.ui.about.AboutEvent
import ua.acclorite.book_story.ui.theme.DefaultTransition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseInfoScaffold(
    library: Library?,
    scrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            DefaultTransition(library != null) {
                LicenseInfoTopBar(
                    library = library!!,
                    scrollBehavior = scrollBehavior,
                    navigateToBrowserPage = navigateToBrowserPage,
                    navigateBack = navigateBack
                )
            }
        }
    ) { paddingValues ->
        DefaultTransition(library != null) {
            LicenseInfoLayout(
                library = library!!,
                paddingValues = paddingValues,
                listState = listState
            )
        }
    }
}