/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.history

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.history.GroupedHistory
import ua.blindmint.codex.presentation.core.components.common.LazyColumnWithScrollbar
import ua.blindmint.codex.presentation.core.constants.providePrimaryScrollbar
import ua.blindmint.codex.presentation.core.util.LocalActivity
import ua.blindmint.codex.presentation.settings.components.SettingsSubcategoryTitle
import ua.blindmint.codex.ui.history.HistoryEvent
import ua.blindmint.codex.ui.theme.DefaultTransition

@Composable
fun HistoryLayout(
    listState: LazyListState,
    history: List<GroupedHistory>,
    snackbarState: SnackbarHostState,
    isLoading: Boolean,
    isRefreshing: Boolean,
    deleteHistoryEntry: (HistoryEvent.OnDeleteHistoryEntry) -> Unit,
    navigateToBookInfo: (Int) -> Unit,
    navigateToReader: (Int) -> Unit,
) {
    val context = LocalActivity.current

    DefaultTransition(visible = !isLoading) {
        LazyColumnWithScrollbar(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            scrollbarSettings = providePrimaryScrollbar(false)
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
                            navigateToBookInfo(historyEntry.bookId)
                        },
                        onTitleClick = {
                            navigateToReader(historyEntry.bookId)
                        },
                        onDeleteClick = {
                            deleteHistoryEntry(
                                HistoryEvent.OnDeleteHistoryEntry(
                                    history = historyEntry,
                                    snackbarState = snackbarState,
                                    context = context,
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