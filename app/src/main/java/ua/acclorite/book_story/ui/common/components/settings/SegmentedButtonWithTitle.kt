/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.settings

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.common.AnimatedVisibility
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.model.ListItem
import ua.acclorite.book_story.ui.settings.components.SettingsSubcategoryTitle

@Composable
fun <T> SegmentedButtonWithTitle(
    modifier: Modifier = Modifier,
    title: String,
    buttons: List<ListItem<T>>,
    enabled: Boolean = true,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 8.dp,
    onClick: (T) -> Unit
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
                    buttons.forEachIndexed { index, item ->
                        SegmentedButton(
                            button = item,
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
                            onClick = { onClick(item.item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> SegmentedButton(
    button: ListItem<T>,
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

        StyledText(
            text = button.title,
            style = button.textStyle().copy(
                color = if (button.selected) colors.activeContentColor
                else colors.inactiveContentColor
            )
        )
    }
}