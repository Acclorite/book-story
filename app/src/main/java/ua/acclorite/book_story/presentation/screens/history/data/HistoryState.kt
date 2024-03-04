package ua.acclorite.book_story.presentation.screens.history.data

import ua.acclorite.book_story.domain.model.GroupedHistory

data class HistoryState(
    val history: List<GroupedHistory> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = true,

    val showDeleteWholeHistoryDialog: Boolean = false,

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val hasFocused: Boolean = false,

    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,

    )
