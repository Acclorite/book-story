/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.history

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.history.HistoryEvent
import ua.acclorite.book_story.presentation.history.model.GroupedHistory
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.ui.common.data.ScrollbarData
import ua.acclorite.book_story.ui.settings.components.SettingsSubcategoryTitle
import ua.acclorite.book_story.ui.theme.DefaultTransition

@Composable
fun HistoryLayout(
    listState: LazyListState,
    history: List<GroupedHistory>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    deleteHistoryEntry: (HistoryEvent.OnDeleteHistoryEntry) -> Unit,
    navigateToBookInfo: (HistoryEvent.OnNavigateToBookInfo) -> Unit,
    navigateToReader: (HistoryEvent.OnNavigateToReader) -> Unit,
) {
    DefaultTransition(visible = !isLoading) {
        LazyColumnWithScrollbar(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            scrollbarSettings = ScrollbarData.primaryScrollbar
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            history.forEachIndexed { index, groupedHistory ->
                item(key = groupedHistory.title) {
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    SettingsSubcategoryTitle(
                        modifier = Modifier.animateItem(),
                        title = when (groupedHistory.title) {
                            "today" -> stringResource(id = R.string.today)
                            "yesterday" -> stringResource(id = R.string.yesterday)
                            else -> groupedHistory.title
                        },
                        padding = 16.dp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(
                    groupedHistory.history,
                    key = { it.id }
                ) { historyEntry ->
                    HistoryItem(
                        historyEntry = historyEntry,
                        isRefreshing = isRefreshing,
                        onBodyClick = {
                            navigateToBookInfo(
                                HistoryEvent.OnNavigateToBookInfo(
                                    historyEntry.book.id
                                )
                            )
                        },
                        onTitleClick = {
                            navigateToReader(
                                HistoryEvent.OnNavigateToReader(
                                    historyEntry.book.id
                                )
                            )
                        },
                        onDeleteClick = {
                            deleteHistoryEntry(
                                HistoryEvent.OnDeleteHistoryEntry(
                                    history = historyEntry
                                )
                            )
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}