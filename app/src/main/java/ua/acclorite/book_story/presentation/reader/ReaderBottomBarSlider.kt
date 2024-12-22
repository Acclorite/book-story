package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.ui.reader.ReaderEvent

@Composable
fun ReaderBottomBarSlider(
    book: Book,
    lockMenu: Boolean,
    listState: LazyListState,
    scroll: (ReaderEvent.OnScroll) -> Unit,
    changeProgress: (ReaderEvent.OnChangeProgress) -> Unit
) {
    Slider(
        value = book.progress,
        enabled = !lockMenu,
        onValueChange = {
            if (listState.layoutInfo.totalItemsCount > 0) {
                scroll(ReaderEvent.OnScroll(it))
                changeProgress(
                    ReaderEvent.OnChangeProgress(
                        progress = it,
                        firstVisibleItemIndex = listState.firstVisibleItemIndex,
                        firstVisibleItemOffset = 0
                    )
                )
            }
        },
        colors = SliderDefaults.colors(
            inactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(0.15f),
            disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
            disabledThumbColor = MaterialTheme.colorScheme.primary,
            disabledInactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(0.15f),
        )
    )
}