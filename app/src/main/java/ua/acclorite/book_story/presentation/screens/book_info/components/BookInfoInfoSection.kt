package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomCoverImage
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoState

/**
 * BookInfo's Info section.
 *
 * @param state [BookInfoState] instance.
 * @param onEvent [BookInfoEvent] callback.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookInfoInfoSection(
    state: State<BookInfoState>,
    onEvent: (BookInfoEvent) -> Unit
) {
    val titleFocusRequester = remember { FocusRequester() }
    val authorFocusRequester = remember { FocusRequester() }

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .height(195.dp)
                .width(130.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    MaterialTheme.shapes.medium
                )
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                    onLongClick = {
                        onEvent(BookInfoEvent.OnShowHideChangeCoverBottomSheet)
                    }
                )
        ) {
            if (state.value.book.coverImage != null) {
                CustomCoverImage(
                    uri = state.value.book.coverImage!!,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = stringResource(id = R.string.cover_image_not_found_content_desc),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.7f)
                        .aspectRatio(1f),
                    tint = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(verticalArrangement = Arrangement.Center) {
            if (!state.value.editTitle) {
                Text(
                    state.value.book.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                            onLongClick = {
                                onEvent(BookInfoEvent.OnShowHideEditTitle)
                            }
                        ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                BasicTextField(
                    maxLines = 4,
                    value = state.value.titleValue,
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    onValueChange = {
                        if (it.length < 100 || it.length < state.value.titleValue.length) {
                            onEvent(BookInfoEvent.OnTitleValueChange(it))
                        }
                    },
                    modifier = Modifier
                        .focusRequester(titleFocusRequester)
                        .onGloballyPositioned {
                            onEvent(BookInfoEvent.OnTitleRequestFocus(titleFocusRequester))
                        },
                    keyboardOptions = KeyboardOptions(KeyboardCapitalization.Words),
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
            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = stringResource(id = R.string.author),
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))

                if (!state.value.editAuthor) {
                    Text(
                        state.value.book.author.asString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {},
                                onLongClick = {
                                    onEvent(BookInfoEvent.OnShowHideEditAuthor)
                                }
                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    BasicTextField(
                        singleLine = true,
                        value = state.value.authorValue,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onValueChange = {
                            if (it.length < 50 || it.length < state.value.authorValue.length) {
                                onEvent(BookInfoEvent.OnAuthorValueChange(it))
                            }
                        },
                        modifier = Modifier
                            .focusRequester(authorFocusRequester)
                            .onGloballyPositioned {
                                onEvent(BookInfoEvent.OnAuthorRequestFocus(authorFocusRequester))
                            },
                        keyboardOptions = KeyboardOptions(KeyboardCapitalization.Words),
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
            }
        }
    }
}