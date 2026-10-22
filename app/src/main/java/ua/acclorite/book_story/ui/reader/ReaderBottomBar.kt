/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.presentation.reader.model.Checkpoint
import ua.acclorite.book_story.ui.common.components.common.IconButton
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.common.helpers.noRippleClickable
import ua.acclorite.book_story.ui.common.model.Direction
import ua.acclorite.book_story.ui.theme.HorizontalExpandingTransition
import ua.acclorite.book_story.ui.theme.readerBarsColor

@Composable
fun ReaderBottomBar(
    book: Book,
    progress: String,
    lockMenu: Boolean,
    checkpoints: List<Checkpoint>,
    bottomBarPadding: Dp,
    restoreCheckpoint: (ReaderEvent.OnRestoreCheckpoint) -> Unit,
    scroll: (ReaderEvent.OnScroll) -> Unit
) {
    val currentOffset = book.progress.currentOffset
    val totalLength = book.progress.totalLength

    val checkpointsProgress = rememberCheckpointsProgress(
        checkpoints = checkpoints,
        totalLength = totalLength,
        currentOffset = currentOffset
    )
    val currentCheckpoint = rememberCurrentCheckpoint(
        checkpoints = checkpoints,
        currentOffset = currentOffset
    )
    val checkpointDirection = rememberCheckpointDirection(
        currentCheckpoint = currentCheckpoint,
        currentOffset = currentOffset
    )
    val onRestoreCheckpointClick = remember(currentCheckpoint) {
        {
            if (currentCheckpoint != null) {
                restoreCheckpoint(
                    ReaderEvent.OnRestoreCheckpoint(
                        currentCheckpoint
                    )
                )
            }
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.readerBarsColor)
            .noRippleClickable(onClick = {})
            .navigationBarsPadding()
            .padding(horizontal = 18.dp)
            .padding(top = 16.dp, bottom = 8.dp + bottomBarPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        StyledText(
            text = progress,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            ReaderBottomBarCheckpoints(
                checkpointDirection = checkpointDirection,
                startArrow = {
                    IconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = R.string.checkpoint_back_content_desc,
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        disableOnClick = false
                    ) {
                        onRestoreCheckpointClick()
                    }
                },
                endArrow = {
                    IconButton(
                        icon = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = R.string.checkpoint_forward_content_desc,
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        disableOnClick = false
                    ) {
                        onRestoreCheckpointClick()
                    }
                },
                slider = {
                    ReaderBottomBarSlider(
                        book = book,
                        lockMenu = lockMenu,
                        scroll = scroll
                    )
                },
                indicator = {
                    ReaderBottomBarCheckpointsIndicator(
                        checkpointsProgress = checkpointsProgress
                    )
                }
            )
        }
    }
}

@Composable
private fun rememberCheckpointsProgress(
    checkpoints: List<Checkpoint>,
    totalLength: Int,
    currentOffset: Int
): List<Float> {
    return remember(checkpoints, totalLength, currentOffset) {
        if (totalLength <= 0) return@remember emptyList()

        val progressScale = 0.987f
        checkpoints.mapNotNull { checkpoint ->
            if (checkpoint.offset == currentOffset) return@mapNotNull null
            (checkpoint.offset / totalLength.toFloat()).coerceIn(0f, 1f) * progressScale
        }
    }
}

@Composable
private fun rememberCurrentCheckpoint(
    checkpoints: List<Checkpoint>,
    currentOffset: Int
): Checkpoint? {
    return remember(checkpoints, currentOffset) {
        checkpoints.lastOrNull { checkpoint ->
            checkpoint.offset != currentOffset
        }
    }
}

@Composable
private fun rememberCheckpointDirection(
    currentCheckpoint: Checkpoint?,
    currentOffset: Int
): Direction {
    return remember(currentCheckpoint, currentOffset) {
        when {
            currentCheckpoint == null -> Direction.NEUTRAL
            currentCheckpoint.offset > currentOffset -> Direction.END
            currentCheckpoint.offset < currentOffset -> Direction.START
            else -> Direction.NEUTRAL
        }
    }
}

@Composable
private fun RowScope.ReaderBottomBarCheckpoints(
    checkpointDirection: Direction,
    startArrow: @Composable () -> Unit,
    endArrow: @Composable () -> Unit,
    slider: @Composable () -> Unit,
    indicator: @Composable () -> Unit
) {
    HorizontalExpandingTransition(
        visible = checkpointDirection == Direction.START,
        startDirection = true
    ) {
        startArrow()
    }

    Box(
        modifier = Modifier.weight(1f),
        contentAlignment = Alignment.CenterStart
    ) {
        slider()
        indicator()
    }

    HorizontalExpandingTransition(
        visible = checkpointDirection == Direction.END,
        startDirection = false
    ) {
        endArrow()
    }
}

@Composable
private fun ReaderBottomBarSlider(
    book: Book,
    lockMenu: Boolean,
    scroll: (ReaderEvent.OnScroll) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDragging by interactionSource.collectIsDraggedAsState()

    val currentProgress = book.progress.getPercentage().coerceIn(0f, 1f)
    var dragProgress by remember { mutableFloatStateOf(currentProgress) }
    var isUserSeeking by remember { mutableStateOf(false) }

    LaunchedEffect(currentProgress) {
        if (!isDragging && !isUserSeeking) {
            dragProgress = currentProgress
        }
    }

    LaunchedEffect(isDragging) {
        if (isDragging) {
            isUserSeeking = true
        }
    }

    Slider(
        value = dragProgress,
        enabled = !lockMenu,
        onValueChange = {
            isUserSeeking = true
            dragProgress = it
        },
        onValueChangeFinished = {
            scroll(ReaderEvent.OnScroll(dragProgress))
            isUserSeeking = false
        },
        interactionSource = interactionSource,
        colors = SliderDefaults.colors(
            inactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(0.15f),
            disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
            disabledThumbColor = MaterialTheme.colorScheme.primary,
            disabledInactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(0.15f),
        )
    )
}

@Composable
private fun ReaderBottomBarCheckpointsIndicator(checkpointsProgress: List<Float>) {
    checkpointsProgress.forEach { checkpoint ->
        Row {
            Spacer(modifier = Modifier.fillMaxWidth(checkpoint))
            Box(
                Modifier
                    .width(4.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(0.5.dp))
                    .background(MaterialTheme.colorScheme.onPrimary.copy(0.7f))
            )
        }
    }
}