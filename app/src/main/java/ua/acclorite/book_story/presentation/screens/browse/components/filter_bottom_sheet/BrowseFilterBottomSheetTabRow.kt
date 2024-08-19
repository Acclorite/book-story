package ua.acclorite.book_story.presentation.screens.browse.components.filter_bottom_sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent

/**
 * Browse Filter Bottom Sheet Tab Row.
 * Lets user switch tabs in bottom sheet.
 *
 * @param onEvent [BrowseEvent] callback.
 * @param pagerState [PagerState].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseFilterBottomSheetTabRow(
    onEvent: (BrowseEvent) -> Unit,
    pagerState: PagerState
) {
    val tabItems = listOf(
        stringResource(id = R.string.general_tab),
        stringResource(id = R.string.filter_tab),
        stringResource(id = R.string.sort_tab)
    )

    PrimaryTabRow(
        modifier = Modifier
            .fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        containerColor = Color.Transparent
    ) {
        tabItems.forEachIndexed { index, tabItem ->
            Tab(
                modifier = Modifier,
                selected = pagerState.currentPage == index,
                onClick = {
                    onEvent(BrowseEvent.OnScrollToFilterPage(index, pagerState))
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                text = {
                    Text(
                        tabItem,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1
                    )
                }
            )
        }
    }
}