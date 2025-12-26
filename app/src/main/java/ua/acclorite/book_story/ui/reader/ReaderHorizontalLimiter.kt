/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import ua.acclorite.book_story.ui.theme.DefaultTransition

@Composable
fun ReaderHorizontalLimiter(
    horizontalLimiter: Boolean,
    horizontalLimiterHeight: Dp,
    horizontalLimiterVerticalOffset: Float,
    horizontalLimiterRulerThickness: Dp,
    horizontalLimiterRuler: Boolean,
    horizontalLimiterDimming: Float,
    horizontalLimiterDimmingColor: Color,
    horizontalLimiterRulerColor: Color
) {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val containerHeight = remember(density, windowInfo.containerSize, horizontalLimiterHeight) {
        with(density) {
            windowInfo.containerSize.height.toDp() - horizontalLimiterHeight
        }
    }
    val verticalLimiterTopOffsetDp = remember(
        containerHeight,
        horizontalLimiterVerticalOffset
    ) {
        containerHeight * horizontalLimiterVerticalOffset
    }
    val verticalLimiterBottomOffsetDp = remember(
        containerHeight,
        horizontalLimiterVerticalOffset
    ) {
        containerHeight * (1 - horizontalLimiterVerticalOffset)
    }

    DefaultTransition(horizontalLimiter) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(verticalLimiterTopOffsetDp)
                    .background(horizontalLimiterDimmingColor.copy(alpha = horizontalLimiterDimming))
            )

            Column(
                modifier = Modifier
                    .height(
                        horizontalLimiterHeight.coerceAtMost(
                            with(density) { windowInfo.containerSize.height.toDp() }
                        )
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (horizontalLimiterRuler) {
                    repeat(2) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalLimiterRulerThickness,
                            horizontalLimiterRulerColor
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(verticalLimiterBottomOffsetDp)
                    .background(horizontalLimiterDimmingColor.copy(alpha = horizontalLimiterDimming))
            )
        }
    }
}