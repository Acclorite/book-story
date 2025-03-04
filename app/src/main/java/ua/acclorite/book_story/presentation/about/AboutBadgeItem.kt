/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.about

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.about.Badge

@Composable
fun AboutBadgeItem(
    badge: Badge,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        if (badge.imageVector == null && badge.drawable != null) {
            Icon(
                modifier = Modifier.size(22.dp),
                painter = painterResource(id = badge.drawable),
                contentDescription = stringResource(id = badge.contentDescription),
                tint = MaterialTheme.colorScheme.tertiary
            )
        } else if (badge.imageVector != null && badge.drawable == null) {
            Icon(
                modifier = Modifier.size(22.dp),
                imageVector = badge.imageVector,
                contentDescription = stringResource(id = badge.contentDescription),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}