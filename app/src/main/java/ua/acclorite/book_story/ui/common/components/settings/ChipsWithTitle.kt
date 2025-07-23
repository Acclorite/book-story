/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.model.ListItem
import ua.acclorite.book_story.ui.settings.components.SettingsSubcategoryTitle

@Composable
fun <T> ChipsWithTitle(
    modifier: Modifier = Modifier,
    title: String,
    chips: List<ListItem<T>>,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 8.dp,
    onClick: (T) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        SettingsSubcategoryTitle(title = title, padding = 0.dp)
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                chips.forEach { item ->
                    FilterChip(
                        modifier = Modifier.height(36.dp),
                        selected = item.selected,
                        label = {
                            StyledText(
                                text = item.title,
                                style = item.textStyle(),
                                maxLines = 1
                            )
                        },
                        onClick = { onClick(item.item) },
                    )
                }
            }
        )
    }
}