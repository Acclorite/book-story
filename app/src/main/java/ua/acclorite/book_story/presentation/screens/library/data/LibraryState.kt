package ua.acclorite.book_story.presentation.screens.library.data

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.domain.model.CategorizedBooks
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.util.Selected

@Immutable
data class LibraryState(
    val books: List<Pair<Book, Selected>> = emptyList(),
    val categorizedBooks: List<CategorizedBooks> = emptyList(),

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
