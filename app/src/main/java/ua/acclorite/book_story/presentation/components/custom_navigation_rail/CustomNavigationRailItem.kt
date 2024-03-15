package ua.acclorite.book_story.presentation.components.custom_navigation_rail

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.NavigationItem

/**
 * Custom Navigation Rail item.
 */
@Composable
fun CustomNavigationRailItem(
    modifier: Modifier = Modifier,
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon = remember(isSelected) {
        if (isSelected) item.selectedIcon
        else item.unselectedIcon
    }

    NavigationRailItem(
        label = {
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        selected = isSelected,
        enabled = !isSelected,
        onClick = { onClick() },
        icon = {
            Icon(
                painter = icon,
                contentDescription = item.title,
                modifier = Modifier.size(24.dp)
            )
        },
        modifier = modifier,
        colors = NavigationRailItemDefaults.colors(
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledIconColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}