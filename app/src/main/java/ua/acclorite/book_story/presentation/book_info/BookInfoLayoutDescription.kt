package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoLayoutDescription(
    book: Book,
    editDescription: Boolean,
    descriptionValue: String,
    editDescriptionMode: (BookInfoEvent.OnEditDescriptionMode) -> Unit,
    editDescriptionValueChange: (BookInfoEvent.OnEditDescriptionValueChange) -> Unit,
    editDescriptionRequestFocus: (BookInfoEvent.OnEditDescriptionRequestFocus) -> Unit,
) {
    val descriptionFocusRequester = remember { FocusRequester() }

    if (!editDescription) {
        Text(
            if (book.description?.isNotBlank() == true) book.description
            else stringResource(id = R.string.error_no_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .noRippleClickable(
                    onClick = {},
                    onLongClick = {
                        editDescriptionMode(BookInfoEvent.OnEditDescriptionMode(true))
                    }
                ),
        )
    } else {
        BasicTextField(
            value = descriptionValue,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            onValueChange = {
                if (it.length < 5000 || it.length < descriptionValue.length) {
                    editDescriptionValueChange(BookInfoEvent.OnEditDescriptionValueChange(it))
                }
            },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .focusRequester(descriptionFocusRequester)
                .onGloballyPositioned {
                    editDescriptionRequestFocus(
                        BookInfoEvent.OnEditDescriptionRequestFocus(
                            descriptionFocusRequester
                        )
                    )
                },
            keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
        ) { innerText ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                innerText()
            }
        }
    }

    Spacer(modifier = Modifier.height(96.dp))
}