package ua.acclorite.book_story.presentation.core.components.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Tooltip.
 * Shows [text] on long press.
 *
 * @param text Text to show.
 * @param padding Space between [content] and tooltip.
 * @param enabled Whether this tooltip is enabled.
 * @param content Content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tooltip(
    text: String,
    padding: Dp = 14.dp,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(padding),
        focusable = false,
        enableUserInput = enabled,
        tooltip = {
            PlainTooltip {
                Text(text = text)
            }
        },
        state = rememberTooltipState()
    ) {
        content()
    }
}