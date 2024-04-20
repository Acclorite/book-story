package ua.acclorite.book_story.presentation.screens.book_info.components.confirm_update_dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Confirm update dialog item.
 */
@Composable
fun BookInfoConfirmUpdateDialogItem(title: String) {
    Text(
        text = "• $title",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
    )
}