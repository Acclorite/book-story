package ua.acclorite.book_story.ui.library

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.library.book.SelectableBook
import ua.acclorite.book_story.domain.util.Dialog

@Immutable
data class LibraryState(
    val books: List<SelectableBook> = emptyList(),

    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,

    val selectedItemsCount: Int = 0,
    val hasSelectedItems: Boolean = false,

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val hasFocused: Boolean = false,

    val dialog: Dialog? = null
)