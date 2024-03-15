package ua.acclorite.book_story.presentation.screens.browse.components.adding_dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.presentation.components.CustomCheckbox

/**
 * Adding dialog item.
 */
@Composable
fun BrowseAddingDialogItem(result: NullableBook, onClick: (Boolean) -> Unit) {
    if (result is NullableBook.NotNull) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(true)
                }
                .padding(vertical = 12.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(0.85f)
            ) {
                Text(
                    text = result.book!!.first.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = result.book.first.author.asString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(modifier = Modifier.weight(0.15f), contentAlignment = Alignment.CenterEnd) {
                CustomCheckbox(selected = result.book!!.second)
            }
        }
    } else {
        val icon =
            if (result.message?.asString() == stringResource(R.string.error_file_encrypted)) {
                Icons.Default.Lock
            } else {
                Icons.Default.Error
            }

        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(false)
                }
                .padding(vertical = 12.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = R.string.error_content_desc),
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = result.fileName!!,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}