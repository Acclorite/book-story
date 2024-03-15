package ua.acclorite.book_story.presentation.screens.library.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.ui.elevation

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

    if (LocalConfiguration.current.screenWidthDp > 450) {
        TabRow(
            modifier = Modifier
                .fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            divider = {
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            },
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .padding(horizontal = 16.dp)
                        .clip(
                            RoundedCornerShape(
                                topStartPercent = 100,
                                topEndPercent = 100
                            )
                        ),
                    color = MaterialTheme.colorScheme.primary
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
    } else {
        ScrollableTabRow(
            modifier = Modifier
                .fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            divider = {
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            },
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .padding(horizontal = 16.dp)
                        .clip(
                            RoundedCornerShape(
                                topStartPercent = 100,
                                topEndPercent = 100
                            )
                        ),
                    color = MaterialTheme.colorScheme.primary
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