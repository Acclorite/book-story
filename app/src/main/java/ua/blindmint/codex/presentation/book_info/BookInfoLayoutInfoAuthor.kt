/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.book_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.presentation.core.components.common.StyledText
import ua.blindmint.codex.presentation.core.util.noRippleClickable
import ua.blindmint.codex.ui.book_info.BookInfoEvent

@Composable
fun BookInfoLayoutInfoAuthor(
    book: Book,
    showAuthorDialog: (BookInfoEvent.OnShowAuthorDialog) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = stringResource(id = R.string.author),
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        StyledText(
            text = book.author.asString(),
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(
                    onClick = {},
                    onLongClick = {
                        showAuthorDialog(BookInfoEvent.OnShowAuthorDialog)
                    }
                ),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            maxLines = 1
        )
    }
}