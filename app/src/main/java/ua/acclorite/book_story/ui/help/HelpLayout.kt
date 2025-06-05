/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.help

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.ui.common.model.Position
import ua.acclorite.book_story.ui.help.data.HelpData

@Composable
fun HelpLayout(
    paddingValues: PaddingValues,
    listState: LazyListState
) {
    LazyColumnWithScrollbar(
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
        state = listState,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        itemsIndexed(
            HelpData.tips,
            key = { _, helpTip -> helpTip.title }
        ) { index, helpTip ->
            HelpItem(
                helpTip = helpTip,
                position = when (index) {
                    0 -> Position.TOP
                    HelpData.tips.lastIndex -> Position.BOTTOM
                    else -> Position.CENTER
                }
            )
        }
    }
}