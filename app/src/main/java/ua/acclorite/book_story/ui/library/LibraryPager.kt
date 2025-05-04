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
import ua.acclorite.book_story.domain.library.Category
import ua.acclorite.book_story.domain.library.CategorySort
import ua.acclorite.book_story.presentation.library.LibraryEvent
import ua.acclorite.book_story.presentation.library.model.LibraryLayout
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.presentation.library.model.LibraryTitlePosition
import ua.acclorite.book_story.presentation.library.model.SelectableBook
import ua.acclorite.book_story.presentation.theme.DefaultTransition
import ua.acclorite.book_story.ui.common.util.compareByWithOrder

@Composable
fun LibraryPager(
    books: List<SelectableBook>,
    pagerState: PagerState,
    categories: List<Category>,
    showDefaultCategory: Boolean,
    perCategorySort: Boolean,
    categoriesSort: List<CategorySort>,
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
    navigateToBrowse: () -> Unit,
    navigateToBookInfo: (id: Int) -> Unit,
    navigateToReader: (id: Int) -> Unit,
) {
    val categorizedBooks = remember(
        books,
        categories,
        categoriesSort,
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

            if (showDefaultCategory) {
                val categoryIds = categories.map { it.id }.toSet()
                categorizedBooks.add(
                    books.filter { book ->
                        book.data.categories.none { category -> category in categoryIds }
                    }.let {
                        if (perCategorySort) {
                            val categorySort = categoriesSort.firstOrNull {
                                it.categoryId == -1
                            } ?: CategorySort(
                                categoryId = -1,
                                sortOrder = LibrarySortOrder.LAST_READ,
                                sortOrderDescending = true
                            )

                            return@let it.sortBooks(
                                categorySort.sortOrder,
                                categorySort.sortOrderDescending
                            )
                        }

                        return@let it
                    }
                )
            }

            categories.sortedBy { it.order }.forEach { category ->
                categorizedBooks.add(
                    books
                        .filter { it.data.categories.any { category.id == it } }
                        .let {
                            if (perCategorySort) {
                                val categorySort = categoriesSort.firstOrNull {
                                    it.categoryId == category.id
                                } ?: CategorySort(
                                    categoryId = category.id,
                                    sortOrder = LibrarySortOrder.LAST_READ,
                                    sortOrderDescending = true
                                )

                                return@let it.sortBooks(
                                    categorySort.sortOrder,
                                    categorySort.sortOrderDescending
                                )
                            }

                            return@let it
                        }
                )
            }

            return@derivedStateOf categorizedBooks.let {
                if (!perCategorySort) {
                    return@let it.map {
                        it.sortBooks(
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
                        navigateToBookInfo = { navigateToBookInfo(book.data.id) },
                        navigateToReader = { navigateToReader(book.data.id) },
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