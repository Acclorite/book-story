/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.ui.settings.library.sort.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder
import ua.acclorite.book_story.ui.common.components.common.StyledText

fun LazyListScope.LibrarySortOption(
    sortOrder: LibrarySortOrder,
    sortOrderDescending: Boolean,
    onChange: (LibrarySortOrder, Boolean) -> Unit
) {
    items(LibrarySortOrder.entries, key = { it.name }) {
        LibrarySortOptionItem(
            item = it,
            isSelected = sortOrder == it,
            isDescending = sortOrderDescending
        ) {
            if (sortOrder == it) {
                onChange(sortOrder, !sortOrderDescending)
            } else {
                onChange(it, true)
            }
        }
    }
}

@Composable
private fun LibrarySortOptionItem(
    item: LibrarySortOrder,
    isSelected: Boolean,
    isDescending: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isDescending) Icons.Default.ArrowDownward
            else Icons.Default.ArrowUpward,
            contentDescription = stringResource(id = R.string.sort_order_content_desc),
            modifier = Modifier
                .size(28.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.secondary
            else Color.Transparent
        )
        Spacer(modifier = Modifier.width(24.dp))

        StyledText(
            text = stringResource(
                when (item) {
                    LibrarySortOrder.NAME -> R.string.sort_order_name
                    LibrarySortOrder.LAST_READ -> R.string.sort_order_last_read
                    LibrarySortOrder.PROGRESS -> R.string.sort_order_progress
                    LibrarySortOrder.AUTHOR -> R.string.sort_order_author
                }
            ),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1
        )
    }
}