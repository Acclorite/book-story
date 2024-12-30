package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookInfoScaffold(
    book: Book,
    listState: LazyListState,
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
    navigateToReader: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .imePadding()
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BookInfoTopBar(
                book = book,
                listState = listState,
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
                navigateBack = navigateBack
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
                navigateToReader = navigateToReader
            )
        }
    }
}