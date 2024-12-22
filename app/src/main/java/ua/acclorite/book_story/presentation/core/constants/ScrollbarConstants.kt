package ua.acclorite.book_story.presentation.core.constants

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.ScrollbarSelectionMode
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun Constants.providePrimaryScrollbar(canSelect: Boolean = true) = ScrollbarSettings(
    thumbUnselectedColor = MaterialTheme.colorScheme.primary,
    thumbSelectedColor = MaterialTheme.colorScheme.primary.copy(0.8f),
    hideDelayMillis = 2000,
    durationAnimationMillis = 300,
    selectionMode = if (canSelect) ScrollbarSelectionMode.Thumb else ScrollbarSelectionMode.Disabled,
    thumbThickness = 8.dp,
    scrollbarPadding = 4.dp
)

@Composable
fun Constants.provideSecondaryScrollbar() = ScrollbarSettings(
    thumbUnselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
    hideDelayMillis = 500,
    durationAnimationMillis = 200,
    thumbMinLength = 0.4f,
    selectionMode = ScrollbarSelectionMode.Disabled,
    thumbThickness = 4.dp,
    thumbShape = RectangleShape,
    hideDisplacement = 0.dp,
    scrollbarPadding = 0.dp
)