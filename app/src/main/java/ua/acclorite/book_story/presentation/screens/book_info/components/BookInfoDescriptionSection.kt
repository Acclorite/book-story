package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoState

/**
 * Description section.
 */
@Composable
fun BookInfoDescriptionSection(state: State<BookInfoState>) {
    SelectionContainer {
        Text(
            if (state.value.book.description?.isNotBlank() == true) state.value.book.description!!
            else stringResource(id = R.string.error_no_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }

    Spacer(modifier = Modifier.height(96.dp))
}