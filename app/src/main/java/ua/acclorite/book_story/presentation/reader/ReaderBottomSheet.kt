package ua.acclorite.book_story.presentation.reader

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.ui.reader.ReaderEvent
import ua.acclorite.book_story.ui.reader.ReaderScreen

@Composable
fun ReaderBottomSheet(
    bottomSheet: BottomSheet?,
    fullscreenMode: Boolean,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit,
    dismissBottomSheet: (ReaderEvent.OnDismissBottomSheet) -> Unit
) {
    when (bottomSheet) {
        ReaderScreen.SETTINGS_BOTTOM_SHEET -> {
            ReaderSettingsBottomSheet(
                fullscreenMode = fullscreenMode,
                menuVisibility = menuVisibility,
                dismissBottomSheet = dismissBottomSheet
            )
        }
    }
}