@file:OptIn(ExperimentalFoundationApi::class)

package com.acclorite.books_history.presentation.screens.library.data

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.focus.FocusRequester
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.Category

sealed class LibraryEvent {
    data class OnPreloadBooks(val books: List<Book>) : LibraryEvent()
    data object OnRefreshList : LibraryEvent()
    data object OnLoadList : LibraryEvent()
    data class OnScrollToPage(val index: Int, val pagerState: PagerState) : LibraryEvent()
    data class OnUpdateCurrentPage(val page: Int) : LibraryEvent()
    data object OnSearchShowHide : LibraryEvent()
    data class OnRequestFocus(val focusRequester: FocusRequester) : LibraryEvent()
    data class OnSearchQueryChange(val query: String) : LibraryEvent()
    data class OnSelectBook(val book: Pair<Book, Boolean>, val select: Boolean? = null) :
        LibraryEvent()

    data object OnClearSelectedBooks : LibraryEvent()
    data object OnShowHideMoveDialog : LibraryEvent()
    data class OnSelectCategory(val category: Category) : LibraryEvent()
    data class OnMoveBooks(val pagerState: PagerState) : LibraryEvent()
    data object OnShowHideDeleteDialog : LibraryEvent()
    data object OnDeleteBooks : LibraryEvent()
    data class OnUpdateBook(val book: Book) : LibraryEvent()
}