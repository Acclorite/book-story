package ua.acclorite.book_story.presentation.screens.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.ChipItem
import ua.acclorite.book_story.presentation.components.CategoryTitle

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun ChipsWithTitle(
    title: String,
    chips: List<ChipItem>,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 8.dp,
    onClick: (ChipItem) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        CategoryTitle(title = title, padding = 0.dp)
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                chips.forEach { item ->
                    FilterChip(
                        modifier = Modifier.height(36.dp),
                        selected = item.selected,
                        enabled = !item.selected,
                        shape = MaterialTheme.shapes.small,
                        border = if (!item.selected) BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline
                        ) else null,
                        colors = ChipDefaults.filterChipColors(
                            backgroundColor = Color.Transparent,
                            disabledBackgroundColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        onClick = { onClick(item) },
                    ) {
                        Text(
                            item.title,
                            style = item.textStyle,
                            color = if (item.selected) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            maxLines = 1
                        )
                    }
                }
            },
        )
    }
}