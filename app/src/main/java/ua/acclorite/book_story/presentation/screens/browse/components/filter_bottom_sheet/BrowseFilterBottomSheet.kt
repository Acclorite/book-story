package ua.acclorite.book_story.presentation.screens.browse.components.filter_bottom_sheet

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.components.CustomBottomSheet
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseState
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseFilterSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseGeneralSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseSortSubcategory

/**
 * Browse Filter Bottom Sheet.
 * Lets user sort, filter and change appearance of the Browse.
 *
 * @param state [BrowseState].
 * @param mainState [MainState].
 * @param onMainEvent [MainEvent] callback.
 * @param onEvent [BrowseEvent] callback.
 */
@Composable
fun BrowseFilterBottomSheet(
    state: State<BrowseState>,
    mainState: State<MainState>,
    onMainEvent: (MainEvent) -> Unit,
    onEvent: (BrowseEvent) -> Unit
) {
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
                    LazyColumn(Modifier.fillMaxSize()) {
                        BrowseGeneralSubcategory(
                            state = mainState,
                            onMainEvent = onMainEvent,
                            showTitle = false,
                            topPadding = 16.dp,
                            bottomPadding = 8.dp + it
                        )
                    }
                }

                1 -> {
                    LazyColumn(Modifier.fillMaxSize()) {
                        BrowseFilterSubcategory(
                            state = mainState,
                            onMainEvent = onMainEvent,
                            showTitle = false,
                            topPadding = 16.dp,
                            bottomPadding = 8.dp + it
                        )
                    }
                }

                2 -> {
                    LazyColumn(Modifier.fillMaxSize()) {
                        BrowseSortSubcategory(
                            state = mainState,
                            onMainEvent = onMainEvent,
                            showTitle = false,
                            topPadding = 16.dp,
                            bottomPadding = 8.dp + it
                        )
                    }
                }
            }
        }
    }
}