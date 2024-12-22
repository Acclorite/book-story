package ua.acclorite.book_story.presentation.licenses

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import com.mikepenz.aboutlibraries.entity.Library

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesContent(
    scrollBehavior: TopAppBarScrollBehavior,
    licenses: List<Library>,
    listState: LazyListState,
    navigateToLicenseInfo: (Library) -> Unit,
    navigateBack: () -> Unit
) {
    LicensesScaffold(
        scrollBehavior = scrollBehavior,
        licenses = licenses,
        listState = listState,
        navigateToLicenseInfo = navigateToLicenseInfo,
        navigateBack = navigateBack
    )
}