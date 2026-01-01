/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.browse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.browse.model.NullableBook
import ua.acclorite.book_story.presentation.browse.model.SelectableNullableBook
import ua.acclorite.book_story.ui.common.components.common.CircularCheckbox
import ua.acclorite.book_story.ui.common.components.common.StyledText

@Composable
fun BrowseAddDialogItem(result: SelectableNullableBook, onClick: () -> Unit) {
    when (result.data) {
        is NullableBook.NotNull -> {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick()
                    }
                    .padding(vertical = 12.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier.weight(1f)
                ) {
                    StyledText(
                        text = result.data.book.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1
                    )
                    StyledText(
                        text = result.data.book.author.asString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1
                    )
                }
                Row {
                    Spacer(modifier = Modifier.width(24.dp))
                    CircularCheckbox(
                        selected = result.selected,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                }
            }
        }

        is NullableBook.Null -> {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick()
                    }
                    .padding(vertical = 12.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = stringResource(id = R.string.error_content_desc),
                    modifier = Modifier.size(26.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                StyledText(
                    text = result.data.fileName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.error
                    ),
                    maxLines = 2
                )
            }
        }
    }
}