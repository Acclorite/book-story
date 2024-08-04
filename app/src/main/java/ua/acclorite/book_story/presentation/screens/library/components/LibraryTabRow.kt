package ua.acclorite.book_story.presentation.screens.library.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.CategorizedBooks
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent

/**
 * Tab row, either scrollable or static, depends on screen width.
 *
 * @param onEvent Event used to Scroll to Page.
 * @param itemCountBackgroundColor Item count background color.
 * @param books The list of all books.
 * @param pagerState [PagerState].
 */
@Composable
fun LibraryTabRow(
    onEvent: (LibraryEvent) -> Unit,
    itemCountBackgroundColor: Color,
    books: List<CategorizedBooks>,
    pagerState: PagerState
) {
    val context = LocalContext.current
    val tabItems = remember(books) {
        listOf(
            Pair(
                context.getString(R.string.reading_tab),
                books.find { it.category == Category.READING }?.books?.size ?: 0
            ),
            Pair(
                context.getString(R.string.already_read_tab),
                books.find { it.category == Category.ALREADY_READ }?.books?.size ?: 0
            ),
            Pair(
                context.getString(R.string.planning_tab),
                books.find { it.category == Category.PLANNING }?.books?.size ?: 0
            ),
            Pair(
                context.getString(R.string.dropped_tab),
                books.find { it.category == Category.DROPPED }?.books?.size ?: 0
            )
        )
    }

    Box(Modifier.fillMaxWidth()) {
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerHighest
        )
        ScrollableTabRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.5.dp),
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            edgePadding = 0.dp,
            divider = {},
            indicator = { tabPositions ->
                if (pagerState.currentPage < tabPositions.size) {
                    val width by animateDpAsState(
                        targetValue = tabPositions[pagerState.currentPage].contentWidth,
                        label = ""
                    )

                    TabRowDefaults.PrimaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        width = width
                    )
                }
            }
        ) {
            tabItems.forEachIndexed { index, tabItem ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        onEvent(LibraryEvent.OnScrollToPage(index, pagerState))
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                tabItem.first,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tabItem.second.toString(),
                                modifier = Modifier
                                    .background(
                                        itemCountBackgroundColor,
                                        MaterialTheme.shapes.medium
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                )
            }
        }
    }
}