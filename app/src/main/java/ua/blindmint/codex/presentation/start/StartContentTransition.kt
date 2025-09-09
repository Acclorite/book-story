/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.start

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.blindmint.codex.domain.navigator.StackEvent
import ua.blindmint.codex.ui.theme.Transitions

@Composable
fun StartContentTransition(
    modifier: Modifier = Modifier,
    targetValue: String,
    stackEvent: StackEvent,
    content: @Composable (String) -> Unit
) {
    AnimatedContent(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        targetState = targetValue,
        transitionSpec = {
            when (stackEvent) {
                StackEvent.Default -> {
                    Transitions.SlidingTransitionIn
                        .togetherWith(Transitions.SlidingTransitionOut)
                }

                StackEvent.Pop -> {
                    Transitions.BackSlidingTransitionIn
                        .togetherWith(Transitions.BackSlidingTransitionOut)
                }
            }
        },
        content = { content(it) }
    )
}