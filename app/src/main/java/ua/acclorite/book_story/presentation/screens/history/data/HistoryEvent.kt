package ua.acclorite.book_story.presentation.screens.history.data

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.focus.FocusRequester
import ua.acclorite.book_story.domain.model.History

sealed class HistoryEvent {
    data object OnRefreshList : HistoryEvent()
    data object OnLoadList : HistoryEvent()
    data object OnShowHideDeleteWholeHistoryDialog : HistoryEvent()
    data object OnDeleteWholeHistory : HistoryEvent()
    data class OnDeleteHistoryElement(
        val historyToDelete: History,
        val snackbarState: SnackbarHostState,
        val context: Context
    ) : HistoryEvent()

    data class OnSearchQueryChange(val query: String) : HistoryEvent()
    data object OnSearchShowHide : HistoryEvent()
    data class OnRequestFocus(val focusRequester: FocusRequester) : HistoryEvent()
    data class OnUpdateScrollIndex(val index: Int) : HistoryEvent()
    data class OnUpdateScrollOffset(val offset: Int) : HistoryEvent()
}