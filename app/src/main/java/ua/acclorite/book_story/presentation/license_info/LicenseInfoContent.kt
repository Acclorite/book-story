package ua.acclorite.book_story.presentation.license_info

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import com.mikepenz.aboutlibraries.entity.Library
import ua.acclorite.book_story.ui.about.AboutEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseInfoContent(
    library: Library?,
    scrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
    navigateToBrowserPage: (AboutEvent.OnNavigateToBrowserPage) -> Unit,
    navigateBack: () -> Unit
) {
    LicenseInfoScaffold(
        library = library,
        scrollBehavior = scrollBehavior,
        listState = listState,
        navigateToBrowserPage = navigateToBrowserPage,
        navigateBack = navigateBack
    )
}