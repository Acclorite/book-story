package ua.acclorite.book_story.presentation.screens.browse.data

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.NullableBook
import java.io.File

@Immutable
data class BrowseState(
    val selectableFiles: List<Pair<File, Boolean>> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val requestPermissionDialog: Boolean = false,
    val showErrorMessage: Boolean = false,

    val selectedItemsCount: Int = 0,
    val hasSelectedItems: Boolean = false,

    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val hasFocused: Boolean = false,

    val showAddingDialog: Boolean = false,
    val selectedBooks: List<NullableBook> = emptyList(),
    val isBooksLoading: Boolean = false
)