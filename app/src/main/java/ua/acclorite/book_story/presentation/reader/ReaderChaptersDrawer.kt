package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.reader.ReaderText.Chapter
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
    val currentChapterIndex = remember(chapters, currentChapter) {
        derivedStateOf {
            chapters.indexOf(currentChapter).takeIf { it != -1 } ?: 0
        }
    }

    ModalDrawer(
        show = show,
        startIndex = currentChapterIndex.value,
        onDismissRequest = { dismissDrawer(ReaderEvent.OnDismissDrawer) },
        header = {
            ModalDrawerTitleItem(
                title = stringResource(id = R.string.chapters)
            )
        }
    ) {
        itemsIndexed(chapters, key = { index, _ -> index }) { index, chapter ->
            val selected = rememberSaveable(index, currentChapterIndex) {
                index == currentChapterIndex.value
            }

            ModalDrawerSelectableItem(
                selected = selected,
                onClick = {
                    scrollToChapter(
                        ReaderEvent.OnScrollToChapter(
                            chapter = chapter
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