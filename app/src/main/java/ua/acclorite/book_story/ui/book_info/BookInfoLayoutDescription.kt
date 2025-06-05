/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.book_info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.book_info.BookInfoEvent
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.helpers.noRippleClickable

@Composable
fun BookInfoLayoutDescription(
    book: Book,
    showDescriptionDialog: (BookInfoEvent.OnShowDescriptionDialog) -> Unit
) {
    val expand = remember { mutableStateOf(false) }
    val expandable = remember(book.description) { (book.description?.length ?: 0) > 50 }

    val backgroundColor = MaterialTheme.colorScheme.surface
    val gradientAnimation = animateColorAsState(
        targetValue = if (expand.value || !expandable) {
            backgroundColor.copy(alpha = 0f)
        } else backgroundColor,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )
    val arrowAnimation = animateFloatAsState(
        if (expand.value) -1f
        else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(
                onClick = {
                    if (expandable) expand.value = !expand.value
                },
                onLongClick = {
                    showDescriptionDialog(BookInfoEvent.OnShowDescriptionDialog)
                }
            )
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .heightIn(0.dp, if (!expand.value && expandable) 60.dp else Dp.Unspecified)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.95f to gradientAnimation.value
                        )
                    )
                }
        ) {
            StyledText(
                text = if (!book.description.isNullOrBlank()) book.description
                else stringResource(id = R.string.error_no_description),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        AnimatedVisibility(
            visible = expandable,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(
                    id = if (expand.value) R.string.show_less_content_desc
                    else R.string.show_more_content_desc
                ),
                modifier = Modifier
                    .size(24.dp)
                    .scale(
                        scaleX = 1f,
                        scaleY = arrowAnimation.value
                    ),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}