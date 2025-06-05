/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoveUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.book_info.BookInfoEvent

@Composable
fun BookInfoLayoutActions(
    showMoveDialog: (BookInfoEvent.OnShowMoveDialog) -> Unit,
    showDeleteDialog: (BookInfoEvent.OnShowDeleteDialog) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .clip(MaterialTheme.shapes.large),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        BookInfoLayoutActionsItem(
            modifier = Modifier.weight(1f),
            alignmentStart = true,
            title = stringResource(id = R.string.move),
            icon = Icons.Default.MoveUp,
            onClick = {
                showMoveDialog(BookInfoEvent.OnShowMoveDialog)
            }
        )

        BookInfoLayoutActionsItem(
            modifier = Modifier.weight(1f),
            alignmentStart = false,
            title = stringResource(id = R.string.delete),
            icon = Icons.Default.Delete,
            onClick = {
                showDeleteDialog(BookInfoEvent.OnShowDeleteDialog)
            }
        )
    }
}