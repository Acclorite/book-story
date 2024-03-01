package com.acclorite.books_history.presentation.screens.book_info.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.Book

@Composable
fun BookInfoDescriptionSection(book: Book) {
    SelectionContainer {
        Text(
            book.description
                ?: stringResource(id = R.string.error_no_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }

    Spacer(modifier = Modifier.height(96.dp))
}