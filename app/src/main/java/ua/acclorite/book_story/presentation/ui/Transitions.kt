package ua.acclorite.book_story.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object Transitions {
    val DefaultTransitionIn = fadeIn(tween(300))
    val DefaultTransitionOut = fadeOut(tween(300))

    val FadeTransitionIn = fadeIn(tween(350))
    val FadeTransitionOut = fadeOut(tween(100))

    val SlidingTransitionIn = fadeIn(tween(350)) +
            slideInHorizontally(tween(350)) { it / 16 }

    val BackSlidingTransitionIn = fadeIn(tween(350)) +
            slideInHorizontally(tween(350)) { -it / 16 }

    val SlidingTransitionOut = fadeOut(tween(250)) +
            slideOutHorizontally(tween(350)) { -it / 16 }

    val BackSlidingTransitionOut = fadeOut(tween(250)) +
            slideOutHorizontally(tween(350)) { it / 16 }
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

@Composable
fun SlidingTransition(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(tween(300)) { -it / 10 } +
                fadeIn(tween(300)),
        exit = slideOutVertically(tween(150)) { -it / 10 } +
                fadeOut(tween(100)),
    ) {
        content()
    }
}










