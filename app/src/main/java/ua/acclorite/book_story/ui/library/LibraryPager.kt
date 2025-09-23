/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.core.helpers.compareByWithOrder
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.presentation.library.LibraryEvent
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryTitlePosition
import ua.acclorite.book_story.presentation.library.model.SelectableBook
import ua.acclorite.book_story.ui.theme.DefaultTransition

@Composable
fun LibraryPager(
    books: List<SelectableBook>,
    pagerState: PagerState,
    categories: List<Category>,
    showDefaultCategory: Boolean,
    perCategorySort: Boolean,
    sortOrder: LibrarySortOrder,
    sortOrderDescending: Boolean,
    layout: LibraryLayout,
    gridSize: Int,
    autoGridSize: Boolean,
    hasSelectedItems: Boolean,
    titlePosition: LibraryTitlePosition,
    readButton: Boolean,
    showProgress: Boolean,
    isLoading: Boolean,
    isRefreshing: Boolean,
    selectBook: (LibraryEvent.OnSelectBook) -> Unit,
    navigateToBrowse: (LibraryEvent.OnNavigateToBrowse) -> Unit,
    navigateToBookInfo: (LibraryEvent.OnNavigateToBookInfo) -> Unit,
    navigateToReader: (LibraryEvent.OnNavigateToReader) -> Unit,
) {
    val categorizedBooks = remember(
        books,
        categories,
        perCategorySort,
        sortOrder,
        sortOrderDescending,
        showDefaultCategory
    ) {
        fun List<SelectableBook>.sortBooks(
            librarySortOrder: LibrarySortOrder,
            librarySortOrderDescending: Boolean
        ): List<SelectableBook> {
            return sortedWith(
                compareByWithOrder(librarySortOrderDescending) { book ->
                    when (librarySortOrder) {
                        LibrarySortOrder.NAME -> book.data.title.trim()
                        LibrarySortOrder.LAST_READ -> book.data.lastOpened
                        LibrarySortOrder.PROGRESS -> book.data.progress
                        LibrarySortOrder.AUTHOR -> book.data.author.getAsString()
                    }
                }
            )
        }

        derivedStateOf {
            val categorizedBooks = mutableListOf<List<SelectableBook>>()

            val categoryIds = categories.map { it.id }.toSet()
            categories
                .filterNot { if (!showDefaultCategory) it.id == -1 else false }
                .sortedBy { it.order }
                .forEach { category ->
                    categorizedBooks.add(
                        books.filter { book ->
                            if (category.id == -1) {
                                book.data.categories.none { it in categoryIds }
                            } else {
                                book.data.categories.any { it == category.id }
                            }
                        }.let { books ->
                            if (perCategorySort) {
                                return@let books.sortBooks(
                                    category.sortOrder,
                                    category.sortOrderDescending
                                )
                            }

                            return@let books
                        }
                    )
                }

            return@derivedStateOf categorizedBooks.let {
                if (!perCategorySort) {
                    return@let it.map { books ->
                        books.sortBooks(
                            sortOrder,
                            sortOrderDescending
                        )
                    }
                }

                return@let it
            }
        }
    }

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
        val category = remember(categorizedBooks, index) {
            derivedStateOf {
                categorizedBooks.value[index]
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            DefaultTransition(visible = !isLoading) {
                LibraryLayout(
                    books = category.value,
                    gridSize = gridSize,
                    autoGridSize = autoGridSize,
                    layout = layout
                ) { book ->
                    LibraryItem(
                        book = book,
                        layout = layout,
                        hasSelectedItems = hasSelectedItems,
                        titlePosition = titlePosition,
                        readButton = readButton,
                        showProgress = showProgress,
                        selectBook = { select ->
                            selectBook(
                                LibraryEvent.OnSelectBook(
                                    id = book.data.id,
                                    select = select
                                )
                            )
                        },
                        navigateToBookInfo = {
                            navigateToBookInfo(
                                LibraryEvent.OnNavigateToBookInfo(
                                    book.data.id
                                )
                            )
                        },
                        navigateToReader = {
                            navigateToReader(
                                LibraryEvent.OnNavigateToReader(
                                    book.data.id
                                )
                            )
                        }
                    )
                }
            }

            LibraryEmptyPlaceholder(
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                isBooksEmpty = category.value.isEmpty(),
                navigateToBrowse = navigateToBrowse
            )
        }
    }
}