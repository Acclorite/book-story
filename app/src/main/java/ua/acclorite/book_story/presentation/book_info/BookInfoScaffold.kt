package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.presentation.core.components.common.Snackbar
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookInfoScaffold(
    book: Book,
    refreshState: PullRefreshState,
    listState: LazyListState,
    snackbarState: SnackbarHostState,
    isUpdating: Boolean,
    checkingForUpdate: Boolean,
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
    showMoreBottomSheet: (BookInfoEvent.OnShowMoreBottomSheet) -> Unit,
    updateData: (BookInfoEvent.OnUpdateData) -> Unit,
    checkForTextUpdate: (BookInfoEvent.OnCheckForTextUpdate) -> Unit,
    cancelTextUpdate: (BookInfoEvent.OnCancelTextUpdate) -> Unit,
    navigateToReader: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .imePadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BookInfoTopBar(
                book = book,
                listState = listState,
                snackbarState = snackbarState,
                isUpdating = isUpdating,
                checkingForUpdate = checkingForUpdate,
                editTitle = editTitle,
                titleValue = titleValue,
                editAuthor = editAuthor,
                authorValue = authorValue,
                editDescription = editDescription,
                descriptionValue = descriptionValue,
                editTitleMode = editTitleMode,
                editAuthorMode = editAuthorMode,
                editDescriptionMode = editDescriptionMode,
                updateData = updateData,
                showMoreBottomSheet = showMoreBottomSheet,
                checkForTextUpdate = checkForTextUpdate,
                cancelTextUpdate = cancelTextUpdate,
                navigateBack = navigateBack
            )
        },
        bottomBar = {
            Snackbar(
                modifier = Modifier.padding(bottom = 70.dp),
                snackbarState = snackbarState
            )
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            BookInfoLayout(
                book = book,
                listState = listState,
                paddingValues = paddingValues,
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
                showChangeCoverBottomSheet = showChangeCoverBottomSheet
            )

            BookInfoFloatingActionButton(
                book = book,
                listState = listState,
                isUpdating = isUpdating,
                navigateToReader = navigateToReader
            )

            BookInfoRefreshIndicator(
                isUpdating = isUpdating,
                checkingForUpdate = checkingForUpdate,
                refreshState = refreshState,
                paddingValues = paddingValues
            )
        }
    }
}