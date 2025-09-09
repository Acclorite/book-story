/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.book_info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.blindmint.codex.R
import ua.blindmint.codex.domain.library.book.Book
import ua.blindmint.codex.presentation.core.components.common.StyledText
import ua.blindmint.codex.presentation.core.util.calculateProgress

@Composable
fun BookInfoLayoutButton(
    book: Book,
    navigateToReader: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        shape = CircleShape,
        onClick = {
            if (book.id != -1) {
                navigateToReader()
            }
        }
    ) {
        StyledText(
            text = if (book.progress == 0f) stringResource(id = R.string.start_reading)
            else stringResource(
                id = R.string.continue_reading_query,
                "${book.progress.calculateProgress(1)}%"
            )
        )
    }
}