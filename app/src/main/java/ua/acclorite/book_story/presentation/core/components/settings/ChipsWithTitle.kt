package ua.acclorite.book_story.presentation.core.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategoryTitle

/**
 * Chips with title. Use list of [ButtonItem]s to display chips.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipsWithTitle(
    modifier: Modifier = Modifier,
    title: String,
    chips: List<ButtonItem>,
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

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                chips.forEach { item ->
                    FilterChip(
                        modifier = Modifier.height(36.dp),
                        selected = item.selected,
                        label = {
                            Text(
                                item.title,
                                style = item.textStyle,
                                maxLines = 1
                            )
                        },
                        onClick = { onClick(item) },
                    )
                }
            },
        )
    }
}