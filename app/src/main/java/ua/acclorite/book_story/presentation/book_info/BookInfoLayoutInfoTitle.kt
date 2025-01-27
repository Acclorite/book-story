package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoLayoutInfoTitle(
    book: Book,
    showTitleDialog: (BookInfoEvent.OnShowTitleDialog) -> Unit
) {
    Text(
        book.title,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(
                onClick = {},
                onLongClick = {
                    showTitleDialog(BookInfoEvent.OnShowTitleDialog)
                }
            ),
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
    )
}