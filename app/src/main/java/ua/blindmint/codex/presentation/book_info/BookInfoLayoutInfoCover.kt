/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.book_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.presentation.core.components.common.AsyncCoverImage
import ua.blindmint.codex.presentation.core.util.noRippleClickable
import ua.blindmint.codex.ui.book_info.BookInfoEvent

@Composable
fun BookInfoLayoutInfoCover(
    book: Book,
    showChangeCoverBottomSheet: (BookInfoEvent.OnShowChangeCoverBottomSheet) -> Unit
) {
    val height = remember { 220.dp }
    val width = remember(height) { height * 0.66f }

    Box(
        modifier = Modifier
            .height(height)
            .width(width)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
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
}