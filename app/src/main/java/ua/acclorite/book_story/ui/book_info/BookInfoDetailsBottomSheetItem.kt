/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.common.IconButton
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun BookInfoDetailsBottomSheetItem(
    label: String,
    text: String,
    editable: Boolean,
    showError: Boolean = false,
    errorMessage: String? = null,
    onEdit: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusable(false),
                value = text,
                onValueChange = {},
                readOnly = true,
                isError = showError,
                label = {
                    StyledText(label)
                }
            )

            if (editable) {
                IconButton(
                    icon = Icons.Default.EditNote,
                    contentDescription = R.string.edit_content_desc,
                    disableOnClick = false,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                ) {
                    onEdit()
                }
            }
        }

        ExpandingTransition(showError && !errorMessage.isNullOrBlank()) {
            StyledText(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp),
                text = errorMessage!!,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}