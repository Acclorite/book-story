package ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet

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
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent

/**
 * Settings bottom sheet tab row. It is used to switch between categories.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsBottomSheetTabRow(
    onEvent: (ReaderEvent) -> Unit,
    pagerState: PagerState
) {
    val tabItems = listOf(
        stringResource(id = R.string.general_tab),
        stringResource(id = R.string.color_tab)
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
                    onEvent(ReaderEvent.OnScrollToSettingsPage(index, pagerState))
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