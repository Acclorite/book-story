/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.browse.model.SelectableFile
import ua.acclorite.book_story.ui.common.components.common.StyledText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RowScope.BrowseListFileItem(file: SelectableFile) {
    val lastModified = rememberSaveable {
        SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
            .format(Date(file.lastModified))
    }

    val sizeBytes = rememberSaveable { file.size }
    val fileSizeKB = rememberSaveable {
        if (sizeBytes > 0) sizeBytes.toDouble() / 1024.0 else 0.0
    }
    val fileSizeMB = rememberSaveable {
        if (sizeBytes > 0) fileSizeKB / 1024.0 else 0.0
    }
    val fileSize = rememberSaveable {
        if (fileSizeMB >= 1.0) "%.2f MB".format(fileSizeMB)
        else if (fileSizeMB > 0.0) "%.2f KB".format(fileSizeKB)
        else "0 KB"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
    ) {
        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    if (file.selected) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(6.dp)
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                contentDescription = stringResource(id = R.string.file_icon_content_desc),
                modifier = Modifier
                    .size(22.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.Center) {
            StyledText(
                text = file.name,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp,
                ),
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(2.dp))
            StyledText(
                text = "$fileSize, $lastModified",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                maxLines = 1
            )
        }
    }
}