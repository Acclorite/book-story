/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.util.noRippleClickable

@Composable
fun BrowseLayoutHeader(
    header: String,
    pinned: Boolean,
    pin: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = header,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(
            modifier = Modifier
                .size(20.dp)
                .noRippleClickable {
                    pin()
                },
            imageVector = if (pinned) Icons.Default.PushPin
            else Icons.Outlined.PushPin,
            contentDescription = stringResource(R.string.pin_content_desc),
            tint = if (pinned) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}