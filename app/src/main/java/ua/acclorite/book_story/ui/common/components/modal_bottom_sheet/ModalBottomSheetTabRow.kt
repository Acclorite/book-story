/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.modal_bottom_sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.common.components.common.StyledText

/**
 * Primary Tab Row. Used in BottomSheet in pair with HorizontalPager.
 * Better to not use too many tabs, as they will overflow.
 *
 * @param selectedTabIndex Selected index, typically pager.currentPage.
 * @param tabs Tabs to be shown, should match HorizontalPager's page count.
 * @param onClick OnClick callback, gives you an index of clicked item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetTabRow(
    selectedTabIndex: Int,
    tabs: List<String>,
    onClick: (index: Int) -> Unit
) {
    PrimaryTabRow(
        modifier = Modifier
            .fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent
    ) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onClick(index)
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                text = {
                    StyledText(
                        text = tabItem,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1
                    )
                }
            )
        }
    }
}