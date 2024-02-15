package com.acclorite.books_history.presentation.screens.library.data

import androidx.compose.runtime.Immutable
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.domain.model.Category

@Immutable
data class LibraryState(
    val books: List<Pair<Book, Boolean>> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,

    val currentPage: Int = 0,

    val selectedItemsCount: Int = 0,
    val hasSelectedItems: Boolean = false,

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val hasFocused: Boolean = false,

    val showMoveDialog: Boolean = false,
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category = Category.READING,

    val showDeleteDialog: Boolean = false,


    )
