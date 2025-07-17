/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.ui.settings.browse.sort.components

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
import ua.acclorite.book_story.presentation.browse.model.BrowseSortOrder
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.helpers.LocalSettings

fun LazyListScope.BrowseSortOption() {
    items(BrowseSortOrder.entries, key = { it.name }) {
        val settings = LocalSettings.current

        BrowseSortOptionItem(
            item = it,
            isSelected = settings.browseSortOrder.value == it,
            isDescending = settings.browseSortOrderDescending.value
        ) {
            if (settings.browseSortOrder.lastValue == it) {
                settings.browseSortOrderDescending.update(!settings.browseSortOrderDescending.lastValue)
            } else {
                settings.browseSortOrderDescending.update(true)
                settings.browseSortOrder.update(it)
            }
        }
    }
}

@Composable
private fun BrowseSortOptionItem(
    item: BrowseSortOrder,
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
                    BrowseSortOrder.NAME -> R.string.sort_order_name
                    BrowseSortOrder.FILE_FORMAT -> R.string.sort_order_file_format
                    BrowseSortOrder.LAST_MODIFIED -> R.string.sort_order_last_modified
                    BrowseSortOrder.FILE_SIZE -> R.string.sort_order_file_size
                }
            ),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1
        )
    }
}