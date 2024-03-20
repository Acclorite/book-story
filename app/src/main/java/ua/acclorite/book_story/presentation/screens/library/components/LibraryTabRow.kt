package ua.acclorite.book_story.presentation.screens.library.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.ui.elevation

/**
 * Tab row, either scrollable or static, depends on screen width.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryTabRow(viewModel: LibraryViewModel, books: List<Book>, pagerState: PagerState) {
    val context = LocalContext.current
    val tabItems = remember(books) {
        listOf(
            Pair(
                context.getString(R.string.reading_tab),
                books.filter { it.category == Category.READING }.size
            ),
            Pair(
                context.getString(R.string.already_read_tab),
                books.filter { it.category == Category.ALREADY_READ }.size
            ),
            Pair(
                context.getString(R.string.planning_tab),
                books.filter { it.category == Category.PLANNING }.size
            ),
            Pair(
                context.getString(R.string.dropped_tab),
                books.filter { it.category == Category.DROPPED }.size
            )
        )
    }

    Box(Modifier.fillMaxWidth()) {
        Divider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant
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
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .padding(horizontal = 16.dp)
                        .padding(end = 5.5.dp)
                        .clip(
                            RoundedCornerShape(
                                100
                            )
                        ),
                    color = MaterialTheme.colorScheme.primary,
                    height = 3.dp
                )
            }
        ) {
            tabItems.forEachIndexed { index, tabItem ->
                Tab(
                    modifier = Modifier,
                    selected = pagerState.currentPage == index,
                    onClick = {
                        viewModel.onEvent(LibraryEvent.OnScrollToPage(index, pagerState))
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
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(MaterialTheme.elevation(6.dp))
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