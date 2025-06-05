/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.calculateProgress
import ua.acclorite.book_story.presentation.library.model.LibraryTitlePosition
import ua.acclorite.book_story.presentation.library.model.SelectableBook
import ua.acclorite.book_story.ui.common.components.common.AsyncCoverImage
import ua.acclorite.book_story.ui.common.components.common.StyledText

@Composable
fun LibraryGridItem(
    book: SelectableBook,
    hasSelectedItems: Boolean,
    titlePosition: LibraryTitlePosition,
    readButton: Boolean,
    showProgress: Boolean,
    selectBook: (select: Boolean?) -> Unit,
    navigateToBookInfo: () -> Unit,
    navigateToReader: () -> Unit
) {
    val backgroundColor = if (book.selected) MaterialTheme.colorScheme.secondary
    else Color.Transparent
    val belowFontColor = if (book.selected) MaterialTheme.colorScheme.onSecondary
    else MaterialTheme.colorScheme.onSurface

    val scrimColor = if (titlePosition == LibraryTitlePosition.INSIDE) {
        MaterialTheme.colorScheme.scrim.copy(0.3f)
    } else Color.Transparent
    val insideFontColor = Color.White.copy(0.85f)

    val progress = rememberSaveable(book.data.progress) {
        "${book.data.progress.calculateProgress(1)}%"
    }

    Column(
        Modifier
            .padding(3.dp)
            .clip(MaterialTheme.shapes.large)
            .background(backgroundColor)
            .padding(3.dp)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f / 1.5f)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .combinedClickable(
                    onClick = {
                        if (hasSelectedItems) selectBook(null)
                        else navigateToBookInfo()
                    },
                    onLongClick = {
                        if (!hasSelectedItems) selectBook(true)
                    }
                )
        ) {
            if (book.data.coverImage != null) {
                AsyncCoverImage(
                    uri = book.data.coverImage,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = stringResource(
                        id = R.string.cover_image_not_found_content_desc
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.7f)
                        .aspectRatio(1f),
                    tint = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            }

            if (showProgress) {
                StyledText(
                    text = progress,
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onTertiary,
                    )
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .drawWithContent {
                        drawRect(
                            brush = Brush.verticalGradient(
                                0f to Color.Transparent,
                                1f to scrimColor
                            )
                        )
                        drawContent()
                    }
                    .padding(6.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                if (titlePosition == LibraryTitlePosition.INSIDE) {
                    StyledText(
                        text = book.data.title,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = insideFontColor
                        ),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                }

                if (readButton) {
                    FilledIconButton(
                        onClick = {
                            if (readButton) navigateToReader()
                        },
                        modifier = Modifier.size(32.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = stringResource(id = R.string.continue_reading_content_desc),
                            Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        if (titlePosition == LibraryTitlePosition.BELOW) {
            Spacer(modifier = Modifier.height(6.dp))
            StyledText(
                text = book.data.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = belowFontColor
                ),
                minLines = 2,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}