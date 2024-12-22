package ua.acclorite.book_story.presentation.browse

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.ui.browse.BrowseEvent
import ua.acclorite.book_story.ui.browse.BrowseScreen

@Composable
fun BrowseBottomSheet(
    bottomSheet: BottomSheet?,
    dismissBottomSheet: (BrowseEvent.OnDismissBottomSheet) -> Unit
) {
    when (bottomSheet) {
        BrowseScreen.FILTER_BOTTOM_SHEET -> {
            BrowseFilterBottomSheet(
                dismissBottomSheet = dismissBottomSheet
            )
        }
    }
}