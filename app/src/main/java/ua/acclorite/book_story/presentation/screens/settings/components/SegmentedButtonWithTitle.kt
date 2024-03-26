package ua.acclorite.book_story.presentation.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.components.CategoryTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedButtonWithTitle(
    title: String,
    buttons: List<ButtonItem>,
    enabled: Boolean,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 8.dp,
    onClick: (ButtonItem) -> Unit
) {
    val colors = SegmentedButtonDefaults.colors()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        CategoryTitle(title = title, padding = 0.dp)

        Spacer(modifier = Modifier.height(8.dp))

        SingleChoiceSegmentedButtonRow {
            buttons.forEachIndexed { index, buttonItem ->
                SegmentedButton(
                    enabled = enabled,
                    selected = buttonItem.selected,
                    onClick = { onClick(buttonItem) },
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
                    colors = SegmentedButtonDefaults.colors(
                        disabledInactiveBorderColor = colors.disabledActiveBorderColor,
                        disabledInactiveContentColor = colors.disabledActiveContentColor,
                        disabledActiveContainerColor = colors.activeContainerColor.copy(0.12f),
                    ),
                    label = {
                        Text(
                            text = buttonItem.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
    }
}








