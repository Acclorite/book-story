package ua.acclorite.book_story.presentation.screens.book_info.components.change_cover_bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Change cover bottom sheet item.
 *
 * @param icon Icon.
 * @param text Text.
 * @param description Description of the item.
 * @param verticalPadding Vertical item padding.
 * @param onClick OnClick callback.
 */
@Composable
fun LazyItemScope.BookInfoChangeCoverBottomSheetItem(
    icon: ImageVector,
    text: String,
    description: String,
    verticalPadding: Dp = 12.dp,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = verticalPadding, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}