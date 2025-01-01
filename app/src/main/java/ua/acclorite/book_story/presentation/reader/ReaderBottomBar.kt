package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.reader.Checkpoint
import ua.acclorite.book_story.domain.reader.ReaderText
import ua.acclorite.book_story.domain.reader.ReaderText.Chapter
import ua.acclorite.book_story.domain.util.Direction
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.core.util.calculateProgress
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.reader.ReaderEvent
import ua.acclorite.book_story.ui.theme.Colors
import ua.acclorite.book_story.ui.theme.HorizontalExpandingTransition

@Composable
fun ReaderBottomBar(
    book: Book,
    text: List<ReaderText>,
    listState: LazyListState,
    lockMenu: Boolean,
    currentChapter: Chapter?,
    currentChapterProgress: Float,
    checkpoint: Checkpoint,
    bottomBarPadding: Dp,
    restoreCheckpoint: (ReaderEvent.OnRestoreCheckpoint) -> Unit,
    scroll: (ReaderEvent.OnScroll) -> Unit,
    changeProgress: (ReaderEvent.OnChangeProgress) -> Unit
) {
    val bookProgress = remember(book.progress) {
        derivedStateOf {
            "${book.progress.calculateProgress(2)}%"
        }
    }
    val chapterProgress = remember(currentChapter, currentChapterProgress) {
        derivedStateOf {
            if (currentChapter == null) return@derivedStateOf ""
            " (${currentChapterProgress.calculateProgress(2)}%)"
        }
    }
    val progress = remember(bookProgress.value, chapterProgress.value) {
        derivedStateOf {
            "${bookProgress.value}${chapterProgress.value}"
        }
    }

    val arrowDirection = remember(checkpoint.index, listState.firstVisibleItemIndex) {
        derivedStateOf {
            val checkpointIndex = checkpoint.index
            val index = listState.firstVisibleItemIndex

            when {
                checkpointIndex > index -> Direction.END
                checkpointIndex < index -> Direction.START
                else -> Direction.NEUTRAL
            }
        }
    }
    val checkpointProgress = remember(checkpoint.index, text.lastIndex) {
        derivedStateOf {
            (checkpoint.index / text.lastIndex.toFloat()) * 0.987f
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .background(Colors.readerSystemBarsColor)
            .noRippleClickable(onClick = {})
            .navigationBarsPadding()
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = progress.value,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalExpandingTransition(
                visible = arrowDirection.value == Direction.START,
                startDirection = true
            ) {
                IconButton(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = R.string.checkpoint_back_content_desc,
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    disableOnClick = false
                ) {
                    restoreCheckpoint(ReaderEvent.OnRestoreCheckpoint)
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                ReaderBottomBarSlider(
                    book = book,
                    lockMenu = lockMenu,
                    listState = listState,
                    scroll = scroll,
                    changeProgress = changeProgress
                )

                if (arrowDirection.value != Direction.NEUTRAL) {
                    ReaderBottomBarSliderIndicator(progress = checkpointProgress.value)
                }
            }

            HorizontalExpandingTransition(
                visible = arrowDirection.value == Direction.END,
                startDirection = false
            ) {
                IconButton(
                    icon = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = R.string.checkpoint_forward_content_desc,
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    disableOnClick = false
                ) {
                    restoreCheckpoint(ReaderEvent.OnRestoreCheckpoint)
                }
            }
        }

        Spacer(Modifier.height(8.dp + bottomBarPadding))
    }
}