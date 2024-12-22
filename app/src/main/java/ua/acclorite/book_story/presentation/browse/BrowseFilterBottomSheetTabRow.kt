package ua.acclorite.book_story.presentation.browse

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.modal_bottom_sheet.ModalBottomSheetTabRow

@Composable
fun BrowseFilterBottomSheetTabRow(
    currentPage: Int,
    scrollToPage: (Int) -> Unit
) {
    val tabItems = listOf(
        stringResource(id = R.string.general_tab),
        stringResource(id = R.string.filter_tab),
        stringResource(id = R.string.sort_tab)
    )

    ModalBottomSheetTabRow(
        selectedTabIndex = currentPage,
        tabs = tabItems
    ) { index ->
        scrollToPage(index)
    }
}