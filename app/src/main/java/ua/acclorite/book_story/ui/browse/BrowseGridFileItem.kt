/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.browse.model.SelectableFile
import ua.acclorite.book_story.ui.common.components.common.CircularCheckbox
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.theme.DefaultTransition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BrowseGridFileItem(file: SelectableFile, hasSelectedItems: Boolean) {
    val lastModified = rememberSaveable {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
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
        if (fileSizeMB >= 1.0) "%.0f MB".format(fileSizeMB)
        else if (fileSizeMB > 0.0) "%.0f KB".format(fileSizeKB)
        else "0 KB"
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    if (file.selected) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                contentDescription = stringResource(id = R.string.file_icon_content_desc),
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f),
                tint = MaterialTheme.colorScheme.secondary
            )

            DefaultTransition(
                visible = hasSelectedItems,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                CircularCheckbox(
                    selected = file.selected,
                    containerColor = MaterialTheme.colorScheme.surface,
                    size = 18.dp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        StyledText(
            text = file.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
            ),
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(2.dp))
        StyledText(
            text = "$fileSize, $lastModified",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            maxLines = 1
        )
    }
}