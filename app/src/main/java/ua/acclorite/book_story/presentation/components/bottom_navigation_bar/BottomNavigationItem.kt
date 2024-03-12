package ua.acclorite.book_story.presentation.components.bottom_navigation_bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.NavigationItem

/**
 * Bottom Navigation Bar Item, uses default [NavigationBarItem].
 */
@Composable
fun RowScope.BottomNavigationBarItem(
    modifier: Modifier = Modifier,
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
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
                painter =
                if (isSelected) item.selectedIcon
                else item.unselectedIcon,
                contentDescription = item.title,
                modifier = Modifier.size(24.dp)
            )
        },
        modifier = modifier,
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledIconColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}