package ua.acclorite.book_story.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.first

/**
 * Custom [AnimatedVisibility]. Prevents bug that animation freezes halfway.
 */
@Composable
fun CustomAnimatedVisibility(
    modifier: Modifier = Modifier,
    visible: Boolean,
    enter: EnterTransition,
    exit: ExitTransition,
    content: @Composable () -> Unit
) {
    val visibleState = remember {
        MutableTransitionState(visible)
    }

    LaunchedEffect(visible) {
        if (visible == visibleState.targetState) {
            return@LaunchedEffect
        }

        if (visibleState.isIdle) {
            visibleState.targetState = visible
            return@LaunchedEffect
        }

        snapshotFlow {
            visibleState.isIdle
        }.first {
            if (it) {
                visibleState.targetState = visible
            }
            it
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        modifier = modifier,
        enter = enter,
        exit = exit,
        label = ""
    ) {
        content()
    }
}