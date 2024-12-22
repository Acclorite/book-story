package ua.acclorite.book_story.presentation.reader

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
import ua.acclorite.book_story.domain.reader.Chapter
import ua.acclorite.book_story.presentation.core.components.modal_drawer.ModalDrawer
import ua.acclorite.book_story.presentation.core.components.modal_drawer.ModalDrawerSelectableItem
import ua.acclorite.book_story.presentation.core.components.modal_drawer.ModalDrawerTitleItem
import ua.acclorite.book_story.presentation.core.util.calculateProgress
import ua.acclorite.book_story.ui.reader.ReaderEvent

@Composable
fun ReaderChaptersDrawer(
    show: Boolean,
    chapters: List<Chapter>,
    currentChapter: Chapter?,
    currentChapterProgress: Float,
    scrollToChapter: (ReaderEvent.OnScrollToChapter) -> Unit,
    dismissDrawer: (ReaderEvent.OnDismissDrawer) -> Unit
) {
    ModalDrawer(
        show = show,
        startIndex = currentChapter?.index ?: 0,
        onDismissRequest = { dismissDrawer(ReaderEvent.OnDismissDrawer) },
        header = {
            ModalDrawerTitleItem(
                title = stringResource(id = R.string.chapters)
            )
        }
    ) {
        items(chapters, key = { it.index }) { chapter ->
            val selected = rememberSaveable(currentChapter) {
                chapter.index == currentChapter?.index
            }

            ModalDrawerSelectableItem(
                selected = selected,
                onClick = {
                    scrollToChapter(
                        ReaderEvent.OnScrollToChapter(
                            chapterStartIndex = chapter.startIndex
                        )
                    )
                    dismissDrawer(ReaderEvent.OnDismissDrawer)
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
                        text = "${currentChapterProgress.calculateProgress(1)}%"
                    )
                }
            }
        }
    }
}