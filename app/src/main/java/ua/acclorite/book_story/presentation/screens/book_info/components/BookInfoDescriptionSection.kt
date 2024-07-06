package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.State
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
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoState

/**
 * Description section.
 *
 * @param state [BookInfoState].
 * @param onEvent [BookInfoEvent] callback.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookInfoDescriptionSection(
    state: State<BookInfoState>,
    onEvent: (BookInfoEvent) -> Unit
) {
    val descriptionFocusRequester = remember { FocusRequester() }

    if (!state.value.editDescription) {
        Text(
            if (state.value.book.description?.isNotBlank() == true) state.value.book.description!!
            else stringResource(id = R.string.error_no_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                    onLongClick = {
                        onEvent(BookInfoEvent.OnShowHideEditDescription)
                    }
                ),
        )
    } else {
        BasicTextField(
            value = state.value.descriptionValue,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            onValueChange = {
                if (it.length < 5000 || it.length < state.value.authorValue.length) {
                    onEvent(BookInfoEvent.OnDescriptionValueChange(it))
                }
            },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .focusRequester(descriptionFocusRequester)
                .onGloballyPositioned {
                    onEvent(BookInfoEvent.OnDescriptionRequestFocus(descriptionFocusRequester))
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