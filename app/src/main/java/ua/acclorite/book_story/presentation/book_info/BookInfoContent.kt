package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.util.BottomSheet
import ua.acclorite.book_story.domain.util.Dialog
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookInfoContent(
    book: Book,
    bottomSheet: BottomSheet?,
    dialog: Dialog?,
    listState: LazyListState,
    canResetCover: Boolean,
    showChangeCoverBottomSheet: (BookInfoEvent.OnShowChangeCoverBottomSheet) -> Unit,
    showDetailsBottomSheet: (BookInfoEvent.OnShowDetailsBottomSheet) -> Unit,
    showTitleDialog: (BookInfoEvent.OnShowTitleDialog) -> Unit,
    actionTitleDialog: (BookInfoEvent.OnActionTitleDialog) -> Unit,
    showAuthorDialog: (BookInfoEvent.OnShowAuthorDialog) -> Unit,
    actionAuthorDialog: (BookInfoEvent.OnActionAuthorDialog) -> Unit,
    showDescriptionDialog: (BookInfoEvent.OnShowDescriptionDialog) -> Unit,
    actionDescriptionDialog: (BookInfoEvent.OnActionDescriptionDialog) -> Unit,
    showMoveDialog: (BookInfoEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (BookInfoEvent.OnShowDeleteDialog) -> Unit,
    actionDeleteDialog: (BookInfoEvent.OnActionDeleteDialog) -> Unit,
    actionMoveDialog: (BookInfoEvent.OnActionMoveDialog) -> Unit,
    changeCover: (BookInfoEvent.OnChangeCover) -> Unit,
    resetCover: (BookInfoEvent.OnResetCover) -> Unit,
    deleteCover: (BookInfoEvent.OnDeleteCover) -> Unit,
    checkCoverReset: (BookInfoEvent.OnCheckCoverReset) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit,
    dismissBottomSheet: (BookInfoEvent.OnDismissBottomSheet) -> Unit,
    navigateToReader: () -> Unit,
    navigateToLibrary: () -> Unit,
    navigateBack: () -> Unit
) {
    BookInfoDialog(
        dialog = dialog,
        book = book,
        actionTitleDialog = actionTitleDialog,
        actionAuthorDialog = actionAuthorDialog,
        actionDescriptionDialog = actionDescriptionDialog,
        actionDeleteDialog = actionDeleteDialog,
        actionMoveDialog = actionMoveDialog,
        dismissDialog = dismissDialog,
        navigateToLibrary = navigateToLibrary,
        navigateBack = navigateBack
    )

    BookInfoBottomSheet(
        bottomSheet = bottomSheet,
        book = book,
        canResetCover = canResetCover,
        changeCover = changeCover,
        resetCover = resetCover,
        deleteCover = deleteCover,
        checkCoverReset = checkCoverReset,
        dismissBottomSheet = dismissBottomSheet
    )

    BookInfoScaffold(
        book = book,
        listState = listState,
        showTitleDialog = showTitleDialog,
        showAuthorDialog = showAuthorDialog,
        showDescriptionDialog = showDescriptionDialog,
        showChangeCoverBottomSheet = showChangeCoverBottomSheet,
        showDetailsBottomSheet = showDetailsBottomSheet,
        showMoveDialog = showMoveDialog,
        showDeleteDialog = showDeleteDialog,
        navigateToReader = navigateToReader,
        navigateBack = navigateBack
    )

    BookInfoBackHandler(
        navigateBack = navigateBack
    )
}