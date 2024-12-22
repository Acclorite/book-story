package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoLayout(
    book: Book,
    listState: LazyListState,
    paddingValues: PaddingValues,
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
    showChangeCoverBottomSheet: (BookInfoEvent.OnShowChangeCoverBottomSheet) -> Unit
) {
    val backgroundCoverImageHeight = remember {
        derivedStateOf {
            paddingValues.calculateTopPadding() + 12.dp + 195.dp
        }
    }

    LazyColumn(
        Modifier
            .fillMaxSize(),
        state = listState
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (book.coverImage != null) {
                    BookInfoLayoutBackground(
                        height = backgroundCoverImageHeight.value,
                        image = book.coverImage
                    )
                }

                Column(Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 12.dp))
                    BookInfoLayoutInfo(
                        book = book,
                        editTitle = editTitle,
                        titleValue = titleValue,
                        editAuthor = editAuthor,
                        authorValue = authorValue,
                        editTitleMode = editTitleMode,
                        editTitleValueChange = editTitleValueChange,
                        editTitleRequestFocus = editTitleRequestFocus,
                        editAuthorMode = editAuthorMode,
                        editAuthorValueChange = editAuthorValueChange,
                        editAuthorRequestFocus = editAuthorRequestFocus,
                        showChangeCoverBottomSheet = showChangeCoverBottomSheet
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            BookInfoLayoutStatistic(book = book)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            BookInfoLayoutDescription(
                book = book,
                editDescription = editDescription,
                descriptionValue = descriptionValue,
                editDescriptionMode = editDescriptionMode,
                editDescriptionValueChange = editDescriptionValueChange,
                editDescriptionRequestFocus = editDescriptionRequestFocus
            )
        }
    }
}