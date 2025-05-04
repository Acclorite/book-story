/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.modal_drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.ui.common.components.common.StyledText

/**
 * Modal Drawer Title Item.
 * Usually displayed at the top of the drawer.
 *
 * @param modifier Modifier to be applied.
 * @param title Title to be shown.
 */
@Composable
fun ModalDrawerTitleItem(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier.height(9.dp))
        StyledText(
            text = title,
            modifier = Modifier.padding(horizontal = 18.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(9.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(4.dp))
    }
}