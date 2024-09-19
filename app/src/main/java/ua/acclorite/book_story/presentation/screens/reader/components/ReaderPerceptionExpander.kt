package ua.acclorite.book_story.presentation.screens.reader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp

/**
 * Reader Perception Expander.
 * Shows two vertical lines on both sides which separate text.
 * This can help you improve reading speed by focusing only on central side.
 *
 * @param perceptionExpander Whether perception expander is enabled.
 * @param sidePadding Side padding applied on top of side text padding.
 * @param thickness Thickness of the line.
 * @param lineColor Color of the line.
 */
@Composable
fun ReaderPerceptionExpander(
    perceptionExpander: Boolean,
    sidePadding: Dp,
    thickness: Dp,
    lineColor: Color
) {
    if (perceptionExpander) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = sidePadding.coerceAtMost(
                        LocalConfiguration.current.screenWidthDp.run {
                            this / 2f - (this * 0.1f)
                        }.dp
                    )
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(2) {
                VerticalDivider(
                    color = lineColor,
                    thickness = thickness
                )
            }
        }
    }
}