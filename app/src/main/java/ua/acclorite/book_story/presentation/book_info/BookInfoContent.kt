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
    editTitle: Boolean,
    titleValue: String,
    editAuthor: Boolean,
    authorValue: String,
    editDescription: Boolean,
    descriptionValue: String,
    editTitleMode: (BookInfoEvent.OnEditTitleMode) -> Unit,
    editTitleValueChange: (BookInfoEvent.OnEditTitleValueChange) -> Unit,
    editTitleRequestFocus: (BookInfoEvent.OnEditTitleRequestFocus) -> Unit,
    editAuthorMode: (BookInfoEvent.OnEditAuthorMode) -> Unit,
    editAuthorValueChange: (BookInfoEvent.OnEditAuthorValueChange) -> Unit,
    editAuthorRequestFocus: (BookInfoEvent.OnEditAuthorRequestFocus) -> Unit,
    editDescriptionMode: (BookInfoEvent.OnEditDescriptionMode) -> Unit,
    editDescriptionValueChange: (BookInfoEvent.OnEditDescriptionValueChange) -> Unit,
    editDescriptionRequestFocus: (BookInfoEvent.OnEditDescriptionRequestFocus) -> Unit,
    showChangeCoverBottomSheet: (BookInfoEvent.OnShowChangeCoverBottomSheet) -> Unit,
    showDetailsBottomSheet: (BookInfoEvent.OnShowDetailsBottomSheet) -> Unit,
    showMoreBottomSheet: (BookInfoEvent.OnShowMoreBottomSheet) -> Unit,
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
    updateData: (BookInfoEvent.OnUpdateData) -> Unit,
    copyToClipboard: (BookInfoEvent.OnCopyToClipboard) -> Unit,
    navigateToReader: () -> Unit,
    navigateToLibrary: () -> Unit,
    navigateBack: () -> Unit
) {
    BookInfoDialog(
        dialog = dialog,
        book = book,
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
        copyToClipboard = copyToClipboard,
        showDetailsBottomSheet = showDetailsBottomSheet,
        showMoveDialog = showMoveDialog,
        showDeleteDialog = showDeleteDialog,
        dismissBottomSheet = dismissBottomSheet
    )

    BookInfoScaffold(
        book = book,
        listState = listState,
        editTitle = editTitle,
        titleValue = titleValue,
        editAuthor = editAuthor,
        authorValue = authorValue,
        editDescription = editDescription,
        descriptionValue = descriptionValue,
        editTitleMode = editTitleMode,
        editTitleValueChange = editTitleValueChange,
        editTitleRequestFocus = editTitleRequestFocus,
        editAuthorMode = editAuthorMode,
        editAuthorValueChange = editAuthorValueChange,
        editAuthorRequestFocus = editAuthorRequestFocus,
        editDescriptionMode = editDescriptionMode,
        editDescriptionValueChange = editDescriptionValueChange,
        editDescriptionRequestFocus = editDescriptionRequestFocus,
        showChangeCoverBottomSheet = showChangeCoverBottomSheet,
        showMoreBottomSheet = showMoreBottomSheet,
        updateData = updateData,
        navigateToReader = navigateToReader,
        navigateBack = navigateBack
    )

    BookInfoBackHandler(
        editTitle = editTitle,
        editAuthor = editAuthor,
        editDescription = editDescription,
        editTitleMode = editTitleMode,
        editAuthorMode = editAuthorMode,
        editDescriptionMode = editDescriptionMode,
        navigateBack = navigateBack
    )
}