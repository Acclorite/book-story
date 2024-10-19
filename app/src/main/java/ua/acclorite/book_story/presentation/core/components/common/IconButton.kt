package ua.acclorite.book_story.presentation.core.components.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

/**
 * Icon Button.
 * Can be disabled after click, so user can't press button fast 2 or 3 times.
 *
 * @param modifier [Modifier].
 * @param icon ImageVector Icon.
 * @param contentDescription Content description (tooltip text).
 * @param disableOnClick Whether button disables after first click.
 * @param enabled Whether button enabled.
 * @param color Color of the Icon.
 * @param onClick OnClick callback.
 */
@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    @StringRes contentDescription: Int,
    disableOnClick: Boolean,
    enabled: Boolean = true,
    color: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    var isClicked by remember { mutableStateOf(false) }
    Tooltip(text = stringResource(id = contentDescription), enabled = enabled) {
        IconButton(
            enabled = enabled && !isClicked,
            modifier = Modifier.focusProperties {
                canFocus = false
            },
            onClick = {
                if (disableOnClick) {
                    isClicked = true
                }
                onClick()
            }
        ) {
            Icon(
                imageVector = icon,
                modifier = modifier,
                contentDescription = stringResource(id = contentDescription),
                tint = color
            )
        }
    }
}

/**
 * Icon Button.
 * Can be disabled after click, so user can't press button fast 2 or 3 times.
 *
 * @param modifier [Modifier].
 * @param icon Drawable Icon.
 * @param contentDescription Content description (tooltip text).
 * @param disableOnClick Whether button disables after first click.
 * @param enabled Whether button enabled.
 * @param color Color of the Icon.
 * @param onClick OnClick callback.
 */
@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes contentDescription: Int,
    disableOnClick: Boolean,
    enabled: Boolean = true,
    color: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    var isClicked by remember { mutableStateOf(false) }
    Tooltip(text = stringResource(id = contentDescription)) {
        IconButton(
            enabled = enabled && !isClicked,
            modifier = Modifier.focusProperties {
                canFocus = false
            },
            onClick = {
                if (disableOnClick) {
                    isClicked = true
                }
                onClick()
            }
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = modifier,
                contentDescription = stringResource(id = contentDescription),
                tint = color
            )
        }
    }
}