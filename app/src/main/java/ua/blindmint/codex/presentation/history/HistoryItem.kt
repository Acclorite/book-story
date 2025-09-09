/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.history.History
import ua.blindmint.codex.presentation.core.components.common.AsyncCoverImage
import ua.blindmint.codex.presentation.core.components.common.IconButton
import ua.blindmint.codex.presentation.core.components.common.StyledText
import ua.blindmint.codex.presentation.core.util.noRippleClickable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LazyItemScope.HistoryItem(
    historyEntry: History,
    isRefreshing: Boolean,
    onBodyClick: () -> Unit,
    onTitleClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val date = rememberSaveable(historyEntry) { Date(historyEntry.time) }
    val pattern = rememberSaveable { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .clickable(enabled = !isRefreshing) {
                onBodyClick()
            }
            .padding(top = 6.dp, bottom = 6.dp, start = 16.dp, end = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .height(90.dp)
                    .width(60.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        RoundedCornerShape(10.dp)
                    )
            ) {
                if (historyEntry.book?.coverImage != null) {
                    AsyncCoverImage(
                        uri = historyEntry.book.coverImage,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
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
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                StyledText(
                    text = historyEntry.book?.title ?: return,
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable(
                            enabled = !isRefreshing,
                            onClick = onTitleClick
                        ),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 21.sp
                    ),
                    maxLines = 2
                )
                StyledText(
                    text = pattern.format(date),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1
                )
            }
        }

        Spacer(Modifier.width(10.dp))
        IconButton(
            icon = Icons.Outlined.Delete,
            contentDescription = R.string.delete_history_element_content_desc,
            disableOnClick = true,
            enabled = !isRefreshing,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            onDeleteClick()
        }
    }
}