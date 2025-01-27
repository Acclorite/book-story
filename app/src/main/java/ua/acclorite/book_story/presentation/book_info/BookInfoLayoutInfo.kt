package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BookInfoLayoutInfo(
    book: Book,
    showTitleDialog: (BookInfoEvent.OnShowTitleDialog) -> Unit,
    showAuthorDialog: (BookInfoEvent.OnShowAuthorDialog) -> Unit,
    showChangeCoverBottomSheet: (BookInfoEvent.OnShowChangeCoverBottomSheet) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BookInfoLayoutInfoCover(
            book = book,
            showChangeCoverBottomSheet = showChangeCoverBottomSheet
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BookInfoLayoutInfoTitle(
                book = book,
                showTitleDialog = showTitleDialog
            )

            BookInfoLayoutInfoAuthor(
                book = book,
                showAuthorDialog = showAuthorDialog
            )

            BookInfoLayoutInfoProgress(
                book = book
            )
        }
    }
}