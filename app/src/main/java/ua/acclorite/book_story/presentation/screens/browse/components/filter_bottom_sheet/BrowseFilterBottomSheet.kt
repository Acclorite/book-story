package ua.acclorite.book_story.presentation.screens.browse.components.filter_bottom_sheet

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.core.components.CustomLazyColumn
import ua.acclorite.book_story.presentation.core.components.LocalBrowseViewModel
import ua.acclorite.book_story.presentation.core.components.custom_bottom_sheet.CustomBottomSheet
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseFilterSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseGeneralSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseSortSubcategory

/**
 * Browse Filter Bottom Sheet.
 * Lets user sort, filter and change appearance of the Browse.
 */
@Composable
fun BrowseFilterBottomSheet() {
    val state = LocalBrowseViewModel.current.state
    val onEvent = LocalBrowseViewModel.current.onEvent

    val pagerState = rememberPagerState(state.value.currentPage) { 3 }

    LaunchedEffect(pagerState.currentPage) {
        onEvent(
            BrowseEvent.OnScrollToFilterPage(
                page = pagerState.currentPage,
                pagerState = null
            )
        )
    }

    CustomBottomSheet(
        hasFixedHeight = true,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        dragHandle = {},
        onDismissRequest = {
            onEvent(BrowseEvent.OnShowHideFilterBottomSheet)
        }
    ) {
        BrowseFilterBottomSheetTabRow(
            onEvent = onEvent,
            pagerState = pagerState
        )

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> {
                    CustomLazyColumn(Modifier.fillMaxSize()) {
                        BrowseGeneralSubcategory(
                            showTitle = false,
                            showDivider = false,
                            topPadding = 16.dp,
                            bottomPadding = 8.dp + it
                        )
                    }
                }

                1 -> {
                    CustomLazyColumn(Modifier.fillMaxSize()) {
                        BrowseFilterSubcategory(
                            showTitle = false,
                            showDivider = false,
                            topPadding = 16.dp,
                            bottomPadding = 8.dp + it
                        )
                    }
                }

                2 -> {
                    CustomLazyColumn(Modifier.fillMaxSize()) {
                        BrowseSortSubcategory(
                            showTitle = false,
                            showDivider = false,
                            topPadding = 16.dp,
                            bottomPadding = 8.dp + it
                        )
                    }
                }
            }
        }
    }
}