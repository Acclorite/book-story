package ua.acclorite.book_story.presentation.components.bottom_navigation_bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.NavigationItem
import ua.acclorite.book_story.presentation.components.CustomTooltip

/**
 * Bottom Navigation Bar Item, uses default [NavigationBarItem].
 */
@Composable
fun RowScope.BottomNavigationBarItem(
    modifier: Modifier = Modifier,
    item: NavigationItem,
    tooltipText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon = remember(isSelected) {
        if (isSelected) item.selectedIcon
        else item.unselectedIcon
    }


    NavigationBarItem(
        label = {
            CustomTooltip(
                text = tooltipText,
                padding = 64.dp
            ) {
                Text(
                    text = item.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        selected = isSelected,
        onClick = { onClick() },
        icon = {
            CustomTooltip(
                text = tooltipText,
                padding = 32.dp
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        modifier = modifier
    )
}