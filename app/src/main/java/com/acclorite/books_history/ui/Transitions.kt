package com.acclorite.books_history.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object Transitions {
    val DefaultTransitionIn = fadeIn(tween(300))
    val DefaultTransitionOut = fadeOut(tween(300))

    val SlidingTransitionIn = fadeIn(tween(300)) +
            slideInHorizontally(tween(300)) { it / 6 }

    val SlidingTransitionOut = fadeOut(tween(300)) +
            slideOutHorizontally(tween(300)) { it / 6 }
}

@Composable
fun DefaultTransition(
    visible: Boolean,
    modifier: Modifier = Modifier,

    content: @Composable (() -> Unit)
) {
    AnimatedVisibility(
        visible,
        modifier = modifier,
        enter = Transitions.DefaultTransitionIn,
        exit = Transitions.DefaultTransitionOut
    ) {
        content()
    }
}











