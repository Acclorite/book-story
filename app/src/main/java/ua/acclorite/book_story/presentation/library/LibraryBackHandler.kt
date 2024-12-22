package ua.acclorite.book_story.presentation.library

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.ui.library.LibraryEvent

@Composable
fun LibraryBackHandler(
    hasSelectedItems: Boolean,
    showSearch: Boolean,
    pagerState: PagerState,
    doublePressExit: Boolean,
    clearSelectedBooks: (LibraryEvent.OnClearSelectedBooks) -> Unit,
    searchVisibility: (LibraryEvent.OnSearchVisibility) -> Unit
) {
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    var shouldExit = rememberSaveable { false }

    BackHandler {
        if (hasSelectedItems) {
            clearSelectedBooks(LibraryEvent.OnClearSelectedBooks)
            return@BackHandler
        }

        if (showSearch) {
            searchVisibility(LibraryEvent.OnSearchVisibility(false))
            return@BackHandler
        }

        if (pagerState.currentPage > 0) {
            scope.launch {
                pagerState.animateScrollToPage(0)
            }
            return@BackHandler
        }

        if (shouldExit || !doublePressExit) {
            activity.finish()
            return@BackHandler
        }

        activity
            .getString(R.string.press_again_toast)
            .showToast(context = activity, longToast = false)
        shouldExit = true

        scope.launch {
            delay(1500)
            shouldExit = false
        }
    }
}