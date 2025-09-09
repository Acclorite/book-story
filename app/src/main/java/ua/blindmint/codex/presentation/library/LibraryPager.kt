/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ua.blindmint.codex.domain.library.category.CategoryWithBooks
import ua.blindmint.codex.ui.library.LibraryEvent
import ua.blindmint.codex.ui.theme.DefaultTransition

@Composable
fun LibraryPager(
    pagerState: PagerState,
    categories: List<CategoryWithBooks>,
    hasSelectedItems: Boolean,
    isLoading: Boolean,
    isRefreshing: Boolean,
    selectBook: (LibraryEvent.OnSelectBook) -> Unit,
    navigateToBrowse: () -> Unit,
    navigateToBookInfo: (id: Int) -> Unit,
    navigateToReader: (id: Int) -> Unit,
) {
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
        val category = remember(categories, index) {
            derivedStateOf {
                categories[index]
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            DefaultTransition(visible = !isLoading) {
                LibraryLayout {
                    items(
                        category.value.books,
                        key = { it.data.id }
                    ) { book ->
                        LibraryItem(
                            book = book,
                            hasSelectedItems = hasSelectedItems,
                            selectBook = { select ->
                                selectBook(
                                    LibraryEvent.OnSelectBook(
                                        id = book.data.id,
                                        select = select
                                    )
                                )
                            },
                            navigateToBookInfo = { navigateToBookInfo(book.data.id) },
                            navigateToReader = { navigateToReader(book.data.id) },
                        )
                    }
                }
            }

            LibraryEmptyPlaceholder(
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                isBooksEmpty = category.value.books.isEmpty(),
                navigateToBrowse = navigateToBrowse
            )
        }
    }
}