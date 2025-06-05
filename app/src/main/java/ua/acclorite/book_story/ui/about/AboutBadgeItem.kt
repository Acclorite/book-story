/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.about

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.ui.about.model.Badge
import ua.acclorite.book_story.ui.common.components.common.IconButton

@Composable
fun AboutBadgeItem(
    badge: Badge,
    onClick: () -> Unit
) {
    if (badge.imageVector == null && badge.drawable != null) {
        IconButton(
            modifier = Modifier.size(22.dp),
            icon = badge.drawable,
            contentDescription = badge.contentDescription,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.tertiary
        ) {
            onClick()
        }
    } else if (badge.imageVector != null && badge.drawable == null) {
        IconButton(
            modifier = Modifier.size(22.dp),
            icon = badge.imageVector,
            contentDescription = badge.contentDescription,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.tertiary
        ) {
            onClick()
        }
    }
}