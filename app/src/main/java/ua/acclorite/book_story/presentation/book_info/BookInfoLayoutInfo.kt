package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.background
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
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.presentation.core.components.common.AsyncCoverImage
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoLayoutInfo(
    book: Book,
    editTitle: Boolean,
    titleValue: String,
    editAuthor: Boolean,
    authorValue: String,
    editTitleMode: (BookInfoEvent.OnEditTitleMode) -> Unit,
    editTitleValueChange: (BookInfoEvent.OnEditTitleValueChange) -> Unit,
    editTitleRequestFocus: (BookInfoEvent.OnEditTitleRequestFocus) -> Unit,
    editAuthorMode: (BookInfoEvent.OnEditAuthorMode) -> Unit,
    editAuthorValueChange: (BookInfoEvent.OnEditAuthorValueChange) -> Unit,
    editAuthorRequestFocus: (BookInfoEvent.OnEditAuthorRequestFocus) -> Unit,
    showChangeCoverBottomSheet: (BookInfoEvent.OnShowChangeCoverBottomSheet) -> Unit
) {
    val titleFocusRequester = remember { FocusRequester() }
    val authorFocusRequester = remember { FocusRequester() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
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
                .noRippleClickable(
                    onClick = {},
                    onLongClick = {
                        showChangeCoverBottomSheet(BookInfoEvent.OnShowChangeCoverBottomSheet)
                    }
                )
        ) {
            if (book.coverImage != null) {
                AsyncCoverImage(
                    uri = book.coverImage,
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
            if (!editTitle) {
                Text(
                    book.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable(
                            onClick = {},
                            onLongClick = {
                                editTitleMode(BookInfoEvent.OnEditTitleMode(true))
                            }
                        ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                BasicTextField(
                    maxLines = 4,
                    value = titleValue,
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    onValueChange = {
                        if (it.length < 100 || it.length < titleValue.length) {
                            editTitleValueChange(BookInfoEvent.OnEditTitleValueChange(it))
                        }
                    },
                    modifier = Modifier
                        .focusRequester(titleFocusRequester)
                        .onGloballyPositioned {
                            editTitleRequestFocus(
                                BookInfoEvent.OnEditTitleRequestFocus(
                                    titleFocusRequester
                                )
                            )
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

                if (!editAuthor) {
                    Text(
                        book.author.asString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable(
                                onClick = {},
                                onLongClick = {
                                    editAuthorMode(BookInfoEvent.OnEditAuthorMode(true))
                                }
                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    BasicTextField(
                        singleLine = true,
                        value = authorValue,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onValueChange = {
                            if (it.length < 50 || it.length < authorValue.length) {
                                editAuthorValueChange(BookInfoEvent.OnEditAuthorValueChange(it))
                            }
                        },
                        modifier = Modifier
                            .focusRequester(authorFocusRequester)
                            .onGloballyPositioned {
                                editAuthorRequestFocus(
                                    BookInfoEvent.OnEditAuthorRequestFocus(
                                        authorFocusRequester
                                    )
                                )
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