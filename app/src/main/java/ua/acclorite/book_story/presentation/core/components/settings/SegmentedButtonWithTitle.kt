package ua.acclorite.book_story.presentation.core.components.settings

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategoryTitle

/**
 * Segmented Button with Title.
 * Uses custom implementation(material3 one has many flaws).
 * Contains [buttons].
 *
 * @param modifier Modifier.
 * @param title Title of the buttons.
 * @param buttons [ButtonItem]s.
 * @param enabled Whether button is enabled.
 * @param horizontalPadding Horizontal item padding.
 * @param verticalPadding Vertical item padding.
 * @param onClick OnClick callback.
 */
@Composable
fun SegmentedButtonWithTitle(
    modifier: Modifier = Modifier,
    title: String,
    buttons: List<ButtonItem>,
    enabled: Boolean = true,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 8.dp,
    onClick: (ButtonItem) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        SettingsSubcategoryTitle(title = title, padding = 0.dp)

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(Modifier.fillMaxWidth()) {
            item {
                Row(
                    Modifier
                        .clip(CircleShape)
                        .border(
                            width = 0.5.dp,
                            color = SegmentedButtonDefaults.colors().activeBorderColor,
                            shape = CircleShape
                        )
                        .padding(0.5.dp)
                ) {
                    buttons.forEachIndexed { index, buttonItem ->
                        SegmentedButton(
                            button = buttonItem,
                            enabled = enabled,
                            shape = when (index) {
                                buttons.lastIndex -> RoundedCornerShape(
                                    topEndPercent = 100,
                                    bottomEndPercent = 100
                                )

                                0 -> RoundedCornerShape(
                                    topStartPercent = 100,
                                    bottomStartPercent = 100
                                )

                                else -> RoundedCornerShape(0)
                            },
                            onClick = { onClick(buttonItem) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Custom Segmented button.
 * Adjusts width based on component name.
 *
 * @param button [ButtonItem].
 * @param enabled Whether can be clicked.
 * @param shape Shape of the button. For proper implementation should have 100% corners on first and last buttons(start and end).
 * @param colors [SegmentedButtonColors].
 * @param onClick OnClick callback.
 */
@Composable
private fun SegmentedButton(
    button: ButtonItem,
    enabled: Boolean,
    shape: RoundedCornerShape,
    colors: SegmentedButtonColors = SegmentedButtonDefaults.colors(),
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .clip(shape)
            .clickable(enabled = enabled && !button.selected) {
                onClick()
            }
            .border(
                width = 0.5.dp,
                color = colors.activeBorderColor,
                shape = shape
            )
            .padding(0.5.dp)
            .background(
                if (button.selected) colors.activeContainerColor
                else Color.Transparent,
                shape = shape
            )
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = button.selected,
            enter = expandHorizontally()
                    + slideInVertically(initialOffsetY = { it / 2 })
                    + scaleIn()
                    + fadeIn(),
            exit = shrinkHorizontally()
                    + slideOutVertically(targetOffsetY = { it / 2 })
                    + scaleOut()
                    + fadeOut()
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(id = R.string.selected_content_desc),
                    modifier = Modifier
                        .size(18.dp),
                    tint = colors.activeContentColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Text(
            text = button.title,
            style = button.textStyle,
            fontWeight = FontWeight.Medium,
            color = if (button.selected) colors.activeContentColor
            else colors.inactiveContentColor
        )
    }
}