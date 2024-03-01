package com.acclorite.books_history.presentation.screens.book_info.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoEvent
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoViewModel
import com.acclorite.books_history.ui.elevation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookInfoInfoSection(viewModel: BookInfoViewModel, book: Book) {
    val state by viewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .height(195.dp)
                .width(130.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.elevation())
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                    onLongClick = {
                        viewModel.onEvent(BookInfoEvent.OnShowHideChangeCoverBottomSheet)
                    }
                )
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Cover Image not found",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f),
                tint = MaterialTheme.elevation(12.dp)
            )

            if (book.coverImage != null) {
                Image(
                    bitmap = book.coverImage.asImageBitmap(),
                    contentDescription = "Cover",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.large),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(verticalArrangement = Arrangement.Center) {
            if (!state.editTitle) {
                Text(
                    book.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                            onLongClick = {
                                viewModel.onEvent(BookInfoEvent.OnShowHideEditTitle)
                            }
                        ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                BasicTextField(
                    value = state.titleValue,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        lineHeight = MaterialTheme.typography.headlineSmall.lineHeight,
                        fontFamily = MaterialTheme.typography.headlineSmall.fontFamily
                    ),
                    onValueChange = {
                        if (it.length < 80) {
                            viewModel.onEvent(BookInfoEvent.OnTitleValueChange(it))
                        }
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onGloballyPositioned {
                            viewModel.onEvent(BookInfoEvent.OnRequestFocus(focusRequester))
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
            SelectionContainer {
                Text(
                    book.author.asString(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}