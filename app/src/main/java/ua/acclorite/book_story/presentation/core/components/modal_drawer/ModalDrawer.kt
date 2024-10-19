package ua.acclorite.book_story.presentation.core.components.modal_drawer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.providePrimaryScrollbar
import ua.acclorite.book_story.presentation.core.util.noRippleClickable

/**
 * Modal Drawer.
 * Follows Material3 guidelines. Does not require to wrap screen's content and works independently.
 * Do not wrap in if(show) { ... }, use [show].
 * Should be placed at the bottom of the composable to be shown at top.
 *
 * @param show Whether should be shown. Animates enter and exit.
 * @param startIndex Start index of the item to show when opening drawer.
 * @param onDismissRequest Dismiss callback. Called when dragging, pressing back or clicking outside drawer.
 * @param header Header of the drawer. Shown at the top.
 * @param content Content inside drawer.
 */
@Composable
fun ModalDrawer(
    show: Boolean,
    startIndex: Int = 0,
    onDismissRequest: () -> Unit,
    header: @Composable () -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    val density = LocalDensity.current
    val offset = remember { mutableFloatStateOf(0f) }
    val offsetDp = remember {
        derivedStateOf {
            with(density) { offset.floatValue.toDp() }
        }
    }

    val animatedScrimColor = animateFloatAsState(
        targetValue = if (show && offsetDp.value > (-60).dp) 0.35f else 0f
    )
    val animatedOffset = animateDpAsState(
        targetValue = offsetDp.value
    )

    LaunchedEffect(show) {
        if (show) offset.floatValue = 0f
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(animatedScrimColor.value))
            .then(
                if (!show) Modifier
                else Modifier.noRippleClickable(onClick = onDismissRequest)
            )
    ) {
        AnimatedVisibility(
            visible = show,
            enter = slideInHorizontally { (-it + with(density) { -20.dp.toPx() }).toInt() },
            exit = slideOutHorizontally { (-it + with(density) { -20.dp.toPx() }).toInt() }
        ) {
            Column(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = animatedOffset.value
                                .toPx()
                                .toInt(),
                            y = 0
                        )
                    }
                    .fillMaxHeight()
                    .width(360.dp)
                    .clip(
                        MaterialTheme.shapes.large.copy(
                            topStart = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp)
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .noRippleClickable {}
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { offset.floatValue = 0f },
                            onDragEnd = {
                                if (offset.floatValue.toDp() < (-60).dp) onDismissRequest()
                                else offset.floatValue = 0f
                            }
                        ) { _, dragAmount ->
                            offset.floatValue = (offset.floatValue + dragAmount)
                                .coerceAtMost(0f)
                        }
                    }
                    .systemBarsPadding()
            ) {
                header()
                LazyColumnWithScrollbar(
                    state = rememberLazyListState(startIndex),
                    modifier = Modifier.fillMaxSize(),
                    scrollbarSettings = Constants.providePrimaryScrollbar(),
                    contentPadding = PaddingValues(vertical = 9.dp)
                ) {
                    content()
                }
            }

            BackHandler {
                onDismissRequest()
            }
        }
    }
}