package ua.acclorite.book_story.ui.history

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.history.GroupedHistory
import ua.acclorite.book_story.domain.util.Dialog

@Immutable
data class HistoryState(
    val history: List<GroupedHistory> = emptyList(),

    val isRefreshing: Boolean = false,
    val isLoading: Boolean = true,

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val hasFocused: Boolean = false,

    val dialog: Dialog? = null
)