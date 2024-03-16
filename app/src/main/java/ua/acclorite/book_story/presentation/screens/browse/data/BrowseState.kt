package ua.acclorite.book_story.presentation.screens.browse.data

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.util.Selected
import java.io.File

@Immutable
data class BrowseState(
    val selectableFiles: List<Pair<File, Selected>> = emptyList(),
    val listState: LazyListState = LazyListState(0, 0),

    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val requestPermissionDialog: Boolean = false,

    val selectedItemsCount: Int = 0,
    val hasSelectedItems: Boolean = false,

    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val hasFocused: Boolean = false,

    val showAddingDialog: Boolean = false,
    val selectedBooks: List<Pair<NullableBook, Selected>> = emptyList(),
    val isBooksLoading: Boolean = false
)