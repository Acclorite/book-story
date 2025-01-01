package ua.acclorite.book_story.presentation.reader

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.reader.ReaderText.Chapter
import ua.acclorite.book_story.domain.util.Drawer
import ua.acclorite.book_story.ui.reader.ReaderEvent
import ua.acclorite.book_story.ui.reader.ReaderScreen

@Composable
fun ReaderDrawer(
    drawer: Drawer?,
    chapters: List<Chapter>,
    currentChapter: Chapter?,
    currentChapterProgress: Float,
    scrollToChapter: (ReaderEvent.OnScrollToChapter) -> Unit,
    dismissDrawer: (ReaderEvent.OnDismissDrawer) -> Unit
) {
    ReaderChaptersDrawer(
        show = drawer == ReaderScreen.CHAPTERS_DRAWER,
        chapters = chapters,
        currentChapter = currentChapter,
        currentChapterProgress = currentChapterProgress,
        scrollToChapter = scrollToChapter,
        dismissDrawer = dismissDrawer
    )
}