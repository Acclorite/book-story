/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.blindmint.codex.presentation.settings.browse.sort.components

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.browse.display.BrowseSortOrder
import ua.blindmint.codex.presentation.core.components.common.StyledText
import ua.blindmint.codex.ui.main.MainEvent
import ua.blindmint.codex.ui.main.MainModel

fun LazyListScope.BrowseSortOption() {
    items(BrowseSortOrder.entries, key = { it.name }) {
        val mainModel = hiltViewModel<MainModel>()
        val state = mainModel.state.collectAsStateWithLifecycle()

        BrowseSortOptionItem(
            item = it,
            isSelected = state.value.browseSortOrder == it,
            isDescending = state.value.browseSortOrderDescending
        ) {
            if (state.value.browseSortOrder == it) {
                mainModel.onEvent(
                    MainEvent.OnChangeBrowseSortOrderDescending(
                        !state.value.browseSortOrderDescending
                    )
                )
            } else {
                mainModel.onEvent(MainEvent.OnChangeBrowseSortOrderDescending(true))
                mainModel.onEvent(MainEvent.OnChangeBrowseSortOrder(it.name))
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
                    BrowseSortOrder.NAME -> R.string.browse_sort_order_name
                    BrowseSortOrder.FILE_FORMAT -> R.string.browse_sort_order_file_format
                    BrowseSortOrder.LAST_MODIFIED -> R.string.browse_sort_order_last_modified
                    BrowseSortOrder.FILE_SIZE -> R.string.browse_sort_order_file_size
                }
            ),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1
        )
    }
}