package ua.acclorite.book_story.presentation.history

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.ui.history.HistoryEvent

@Composable
fun HistoryBackHandler(
    showSearch: Boolean,
    searchVisibility: (HistoryEvent.OnSearchVisibility) -> Unit,
    navigateToLibrary: () -> Unit
) {
    BackHandler {
        if (showSearch) {
            searchVisibility(HistoryEvent.OnSearchVisibility(false))
            return@BackHandler
        }

        navigateToLibrary()
    }
}