package ua.acclorite.book_story.presentation.core.components.bottom_navigation_bar

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.NavigationItem
import ua.acclorite.book_story.presentation.core.components.CustomTooltip

/**
 * Bottom Navigation Bar Item, uses default [NavigationBarItem].
 *
 * @param modifier Modifier.
 * @param item [NavigationItem].
 * @param isSelected Whether this screen is currently selected.
 * @param onClick OnClick callback.
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
            CustomTooltip(
                text = stringResource(id = item.tooltip),
                padding = 64.dp
            ) {
                Text(
                    text = stringResource(id = item.title),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        selected = isSelected,
        onClick = { onClick() },
        icon = {
            CustomTooltip(
                text = stringResource(id = item.tooltip),
                padding = 32.dp
            ) {
                Crossfade(
                    targetState = isSelected,
                    animationSpec = tween(150),
                    label = ""
                ) {
                    if (it) {
                        Icon(
                            painter = painterResource(id = item.selectedIcon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = item.unselectedIcon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        },
        modifier = modifier
    )
}