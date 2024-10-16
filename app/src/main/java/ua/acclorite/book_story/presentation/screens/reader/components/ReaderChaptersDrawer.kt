package ua.acclorite.book_story.presentation.screens.reader.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.custom_drawer.CustomModalDrawer
import ua.acclorite.book_story.presentation.core.components.custom_drawer.CustomModalDrawerSelectableItem
import ua.acclorite.book_story.presentation.core.components.custom_drawer.CustomModalDrawerTitleItem
import ua.acclorite.book_story.presentation.core.util.calculateProgress
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel

/**
 * Reader Chapters Drawer.
 * Shows the list of all chapter, current chapter and lets user to go to specific chapter.
 */
@Composable
fun ReaderChaptersDrawer() {
    val state = ReaderViewModel.getState()
    val onEvent = ReaderViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()

    CustomModalDrawer(
        show = state.value.showChaptersDrawer,
        startIndex = state.value.currentChapter?.index ?: 0,
        onDismissRequest = { onEvent(ReaderEvent.OnShowHideChaptersDrawer(false)) },
        header = {
            CustomModalDrawerTitleItem(
                title = stringResource(id = R.string.chapters)
            )
        }
    ) {
        items(state.value.book.chapters, key = { it.index }) { chapter ->
            val selected = rememberSaveable(state.value.currentChapter) {
                chapter.index == state.value.currentChapter?.index
            }

            CustomModalDrawerSelectableItem(
                selected = selected,
                onClick = {
                    onEvent(
                        ReaderEvent.OnScrollToChapter(
                            chapterStartIndex = chapter.startIndex,
                            refreshList = { book ->
                                onLibraryEvent(LibraryEvent.OnUpdateBook(book))
                                onHistoryEvent(HistoryEvent.OnUpdateBook(book))
                            }
                        )
                    )
                    onEvent(ReaderEvent.OnShowHideChaptersDrawer(false))
                }
            ) {
                Text(
                    text = chapter.title,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (selected) {
                    Spacer(modifier = Modifier.width(18.dp))
                    Text(
                        text = "${state.value.currentChapterProgress.calculateProgress(1)}%"
                    )
                }
            }
        }
    }
}