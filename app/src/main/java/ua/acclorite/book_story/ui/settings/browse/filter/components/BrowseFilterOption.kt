/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.ui.settings.browse.filter.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.core.data.ExtensionsData
import ua.acclorite.book_story.core.helpers.toggle
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.helpers.LocalSettings

fun LazyListScope.BrowseFilterOption() {
    items(ExtensionsData.fileExtensions, key = { it }) {
        val settings = LocalSettings.current

        BrowseFilterOptionItem(
            item = it,
            isSelected = settings.browseIncludedFilterItems.value.any { item -> item == it }
        ) {
            settings.browseIncludedFilterItems.update(
                settings.browseIncludedFilterItems.lastValue.toggle(it)
            )
        }
    }
}

@Composable
private fun BrowseFilterOptionItem(
    item: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                checkmarkColor = MaterialTheme.colorScheme.onSecondary
            ),
            onCheckedChange = { onClick() }
        )
        Spacer(modifier = Modifier.width(14.dp))

        StyledText(
            text = item,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1
        )
    }
}