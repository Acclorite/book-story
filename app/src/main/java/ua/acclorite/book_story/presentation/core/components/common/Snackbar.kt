package ua.acclorite.book_story.presentation.core.components.common

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Snackbar with vertical Swipe-To-Dismiss and animation.
 * It is recommended to put it in Scaffold bottomBar(this way padding is the same at all sides)
 *
 * @param modifier Modifier to be applied.
 * @param snackbarState Regular [SnackbarHostState], use ..remember { SnackbarHostState() }
 */
@Composable
fun Snackbar(modifier: Modifier = Modifier, snackbarState: SnackbarHostState) {
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarData by remember { mutableStateOf(snackbarState.currentSnackbarData) }
    var offset by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val isDragged by mutableInteractionSource.collectIsDraggedAsState()

    val alpha by remember {
        derivedStateOf {
            1f - ((offset / 2f) / 100f)
        }
    }

    LaunchedEffect(offset, isDragged) {
        val offsetDp = with(density) { offset.toDp() }
        if (offsetDp > 35.dp && !isDragged) {
            snackbarState.currentSnackbarData?.dismiss()
            return@LaunchedEffect
        }

        if (offsetDp <= 35.dp && !isDragged) {
            offset = 0f
        }
    }
    LaunchedEffect(snackbarState.currentSnackbarData) {
        val data = snackbarState.currentSnackbarData

        if (data == null) {
            showSnackbar = false
            return@LaunchedEffect
        }

        offset = 0f
        snackbarData = data
        showSnackbar = true
    }

    AnimatedVisibility(
        visible = showSnackbar,
        enter = slideInVertically(tween(350)) { it } + fadeIn(tween(350)),
        exit = slideOutVertically(tween(200)) { it } + fadeOut(tween(100))
    ) {
        Box(
            modifier = modifier
                .offset { IntOffset(0, offset.roundToInt()) }
                .padding(vertical = 4.dp)
                .fillMaxWidth()
                .draggable(
                    interactionSource = mutableInteractionSource,
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState {
                        offset = (offset + it).coerceAtLeast(0f)
                    }
                )
        ) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(alpha),
                snackbarData = snackbarData!!,
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}