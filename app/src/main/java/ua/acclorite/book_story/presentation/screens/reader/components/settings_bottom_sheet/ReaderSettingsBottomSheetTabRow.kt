package ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalReaderViewModel
import ua.acclorite.book_story.presentation.core.components.custom_bottom_sheet.CustomBottomSheetTabRow
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent

/**
 * Settings bottom sheet tab row.
 * It is used to switch between categories.
 *
 * @param pagerState PagerState to use along with TabRow.
 */
@Composable
fun ReaderSettingsBottomSheetTabRow(pagerState: PagerState) {
    val onEvent = LocalReaderViewModel.current.onEvent

    val tabItems = listOf(
        stringResource(id = R.string.general_tab),
        stringResource(id = R.string.reader_tab),
        stringResource(id = R.string.color_tab)
    )

    CustomBottomSheetTabRow(
        selectedTabIndex = pagerState.currentPage,
        tabs = tabItems
    ) { index ->
        onEvent(ReaderEvent.OnScrollToSettingsPage(index, pagerState))
    }
}