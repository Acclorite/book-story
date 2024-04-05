package ua.acclorite.book_story.presentation.components.custom_navigation_rail

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.NavigationItem
import ua.acclorite.book_story.presentation.components.CustomTooltip

/**
 * Custom Navigation Rail item.
 */
@Composable
fun CustomNavigationRailItem(
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

    NavigationRailItem(
        label = {
            CustomTooltip(
                text = tooltipText,
                padding = 48.dp
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
                padding = 16.dp
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