package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.presentation.data.removeDigits
import ua.acclorite.book_story.presentation.data.removeTrailingZero

@Composable
fun BookInfoStatisticSection(book: Book) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "${
                (book.progress * 100)
                    .toDouble()
                    .removeDigits(1)
                    .removeTrailingZero()
            }%",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            book.progress.coerceAtLeast(0.01f),
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.05f)
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(
                id = R.string.you_read_query,
                (book.progress * 100)
                    .toDouble()
                    .removeDigits(1)
                    .removeTrailingZero() + "%"
            ) + " " + stringResource(
                if (book.progress > 0.2f) R.string.read_keep
                else R.string.read_more
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}