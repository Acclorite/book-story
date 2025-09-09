/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.library

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
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SyncProblem
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.library.book.SelectableBook
import ua.blindmint.codex.domain.library.book.SyncStatus
import ua.blindmint.codex.presentation.core.components.common.AsyncCoverImage
import ua.blindmint.codex.presentation.core.components.common.StyledText
import ua.blindmint.codex.presentation.core.util.calculateProgress

@Composable
fun LazyGridItemScope.LibraryItem(
    book: SelectableBook,
    hasSelectedItems: Boolean,
    selectBook: (select: Boolean?) -> Unit,
    navigateToBookInfo: () -> Unit,
    navigateToReader: () -> Unit
) {
    val backgroundColor = if (book.selected) MaterialTheme.colorScheme.secondary
    else Color.Transparent
    val fontColor = if (book.selected) MaterialTheme.colorScheme.onSecondary
    else MaterialTheme.colorScheme.onSurface

    val progress = rememberSaveable(book.data.progress) {
        "${book.data.progress.calculateProgress(1)}%"
    }

    @Composable
    fun SyncStatusIconComposable(syncStatus: SyncStatus) {
        val (icon, tint) = when (syncStatus) {
            SyncStatus.NOT_SYNCED -> Icons.Default.Sync to MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
            SyncStatus.SYNCING -> Icons.Default.Sync to MaterialTheme.colorScheme.secondary
            SyncStatus.SYNCED -> Icons.Default.Sync to MaterialTheme.colorScheme.primary
            SyncStatus.SYNC_ERROR -> Icons.Default.SyncProblem to MaterialTheme.colorScheme.error
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = tint
        )
    }

    Column(
        Modifier
            .animateItem()
            .padding(3.dp)
            .background(
                backgroundColor,
                MaterialTheme.shapes.large
            )
            .padding(3.dp)
            .padding(bottom = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f / 1.5f)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    MaterialTheme.shapes.medium
                )
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

            Row(
                modifier = Modifier
                    .padding(6.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.small)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                StyledText(
                    text = progress,
                    style = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                )
                SyncStatusIconComposable(book.data.syncStatus)
            }

            FilledIconButton(
                onClick = { navigateToReader() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .size(32.dp),
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
        Spacer(modifier = Modifier.height(6.dp))
        StyledText(
            text = book.data.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = fontColor
            ),
            minLines = 2,
            maxLines = 2
        )
    }
}