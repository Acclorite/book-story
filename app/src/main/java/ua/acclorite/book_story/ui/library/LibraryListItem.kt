/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.calculateProgress
import ua.acclorite.book_story.presentation.library.model.SelectableBook
import ua.acclorite.book_story.ui.common.components.common.AsyncCoverImage
import ua.acclorite.book_story.ui.common.components.common.StyledText

@Composable
fun LibraryListItem(
    book: SelectableBook,
    hasSelectedItems: Boolean,
    readButton: Boolean,
    showProgress: Boolean,
    selectBook: (select: Boolean?) -> Unit,
    navigateToBookInfo: () -> Unit,
    navigateToReader: () -> Unit
) {
    val backgroundColor = if (book.selected) MaterialTheme.colorScheme.secondaryContainer
    else Color.Transparent
    val fontColor = if (book.selected) MaterialTheme.colorScheme.onSecondaryContainer
    else MaterialTheme.colorScheme.onSurface

    val progress = rememberSaveable(book.data.progress) {
        "${book.data.progress.calculateProgress(1)}%"
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    if (hasSelectedItems) selectBook(null)
                    else navigateToBookInfo()
                },
                onLongClick = {
                    if (!hasSelectedItems) selectBook(true)
                }
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            if (book.data.coverImage != null) {
                AsyncCoverImage(
                    uri = book.data.coverImage,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.small)
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

        Spacer(modifier = Modifier.width(16.dp))

        StyledText(
            text = book.data.title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = fontColor
            ),
            maxLines = 2
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (showProgress) {
            Spacer(modifier = Modifier.width(8.dp))

            StyledText(
                text = progress,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onTertiary
                )
            )
        }

        if (readButton) {
            Spacer(modifier = Modifier.width(8.dp))

            FilledIconButton(
                onClick = { if (readButton) navigateToReader() },
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
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}