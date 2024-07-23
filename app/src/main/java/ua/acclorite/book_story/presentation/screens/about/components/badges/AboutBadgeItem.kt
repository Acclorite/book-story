package ua.acclorite.book_story.presentation.screens.about.components.badges

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.Badge
import ua.acclorite.book_story.presentation.components.CustomIconButton

/**
 * About Badge Item.
 *
 * @param badge [Badge].
 * @param onClick OnClick callback.
 */
@Composable
fun AboutBadgeItem(
    badge: Badge,
    onClick: () -> Unit
) {
    if (badge.imageVector == null && badge.drawable != null) {
        CustomIconButton(
            modifier = Modifier.size(22.dp),
            icon = badge.drawable,
            contentDescription = badge.contentDescription,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.primary
        ) {
            onClick()
        }
    } else if (badge.imageVector != null && badge.drawable == null) {
        CustomIconButton(
            modifier = Modifier.size(22.dp),
            icon = badge.imageVector,
            contentDescription = badge.contentDescription,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.primary
        ) {
            onClick()
        }
    }
}