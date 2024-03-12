package ua.acclorite.book_story.presentation.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.components.CategoryTitle

/**
 * Checkbox with title.
 */
@Composable
fun CheckboxWithTitle(
    selected: Boolean,
    title: String,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 12.dp,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = null,
        )
        Spacer(modifier = Modifier.width(24.dp))
        CategoryTitle(title = title, padding = 0.dp)
    }
}