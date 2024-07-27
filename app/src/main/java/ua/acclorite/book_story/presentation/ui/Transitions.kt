package ua.acclorite.book_story.presentation.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import kotlin.math.roundToInt

object Transitions {
    val DefaultTransitionIn = fadeIn(tween(300))
    val DefaultTransitionOut = fadeOut(tween(300))

    val FadeTransitionIn = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.98f)
    val FadeTransitionOut = fadeOut(tween(50))

    val SlidingTransitionIn = fadeIn(tween(350)) +
            slideInHorizontally(tween(350)) { it / 16 }
    val BackSlidingTransitionIn = fadeIn(tween(350)) +
            slideInHorizontally(tween(350)) { -it / 16 }

    val SlidingTransitionOut = fadeOut(tween(250)) +
            slideOutHorizontally(tween(350)) { -it / 16 }
    val BackSlidingTransitionOut = fadeOut(tween(250)) +
            slideOutHorizontally(tween(350)) { it / 16 }

    val NoEnterAnimation = fadeIn(tween(0))
    val NoExitAnimation = fadeOut(tween(0))
}

@Composable
fun DefaultTransition(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    CustomAnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = Transitions.DefaultTransitionIn,
        exit = Transitions.DefaultTransitionOut
    ) {
        content()
    }
}

@Composable
fun <T> T.DefaultTransition(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable T.() -> Unit
) {
    CustomAnimatedVisibility(
        visible = visible,
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
    val density = LocalDensity.current
    CustomAnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(tween(300)) {
            with(density) { -12.dp.toPx() }.roundToInt()
        } + fadeIn(tween(300)),
        exit = slideOutVertically(tween(150)) {
            with(density) { -10.dp.toPx() }.roundToInt()
        } + fadeOut(tween(100)),
        content = content
    )
}