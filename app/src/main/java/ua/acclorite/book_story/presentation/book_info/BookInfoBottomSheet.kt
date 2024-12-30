package ua.acclorite.book_story.presentation.book_info

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.ui.book_info.BookInfoEvent
import ua.acclorite.book_story.ui.book_info.BookInfoScreen

@Composable
fun BookInfoBottomSheet(
    bottomSheet: BottomSheet?,
    book: Book,
    canResetCover: Boolean,
    changeCover: (BookInfoEvent.OnChangeCover) -> Unit,
    resetCover: (BookInfoEvent.OnResetCover) -> Unit,
    deleteCover: (BookInfoEvent.OnDeleteCover) -> Unit,
    checkCoverReset: (BookInfoEvent.OnCheckCoverReset) -> Unit,
    copyToClipboard: (BookInfoEvent.OnCopyToClipboard) -> Unit,
    showDetailsBottomSheet: (BookInfoEvent.OnShowDetailsBottomSheet) -> Unit,
    showMoveDialog: (BookInfoEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (BookInfoEvent.OnShowDeleteDialog) -> Unit,
    dismissBottomSheet: (BookInfoEvent.OnDismissBottomSheet) -> Unit
) {
    when (bottomSheet) {
        BookInfoScreen.CHANGE_COVER_BOTTOM_SHEET -> {
            BookInfoChangeCoverBottomSheet(
                book = book,
                canResetCover = canResetCover,
                changeCover = changeCover,
                resetCover = resetCover,
                deleteCover = deleteCover,
                checkCoverReset = checkCoverReset,
                dismissBottomSheet = dismissBottomSheet
            )
        }

        BookInfoScreen.MORE_BOTTOM_SHEET -> {
            BookInfoMoreBottomSheet(
                showDetailsBottomSheet = showDetailsBottomSheet,
                showDeleteDialog = showDeleteDialog,
                showMoveDialog = showMoveDialog,
                dismissBottomSheet = dismissBottomSheet
            )
        }

        BookInfoScreen.DETAILS_BOTTOM_SHEET -> {
            BookInfoDetailsBottomSheet(
                book = book,
                copyToClipboard = copyToClipboard,
                dismissBottomSheet = dismissBottomSheet
            )
        }
    }
}