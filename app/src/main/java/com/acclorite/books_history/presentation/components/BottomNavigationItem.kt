package com.acclorite.books_history.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.acclorite.books_history.domain.model.NavigationBarItem

/**
 * Bottom Navigation Bar Item, uses default [NavigationBarItem].
 */
@Composable
fun BottomNavigationBarItem(
    modifier: Modifier,
    item: NavigationBarItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(modifier.fillMaxHeight()) {
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
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledIconColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}