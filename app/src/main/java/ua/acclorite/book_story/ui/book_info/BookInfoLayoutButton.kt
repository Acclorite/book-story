/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.helpers.calculateProgress
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.book_info.BookInfoEvent
import ua.acclorite.book_story.ui.common.components.common.StyledText

@Composable
fun BookInfoLayoutButton(
    book: Book,
    navigateToReader: (BookInfoEvent.OnNavigateToReader) -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        shape = CircleShape,
        onClick = {
            navigateToReader(BookInfoEvent.OnNavigateToReader)
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