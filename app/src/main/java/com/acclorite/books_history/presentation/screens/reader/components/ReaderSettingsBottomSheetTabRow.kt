package com.acclorite.books_history.presentation.screens.reader.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.screens.reader.data.ReaderEvent
import com.acclorite.books_history.presentation.screens.reader.data.ReaderViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReaderSettingsBottomSheetTabRow(viewModel: ReaderViewModel, pagerState: PagerState) {
    val tabItems = listOf(
        stringResource(id = R.string.general_tab),
        stringResource(id = R.string.color_tab)
    )

    TabRow(
        modifier = Modifier
            .fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        divider = {
            Divider(color = MaterialTheme.colorScheme.surfaceVariant)
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .padding(horizontal = 64.dp)
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
                    viewModel.onEvent(ReaderEvent.OnScrollToSettingsPage(index, pagerState))
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface,
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