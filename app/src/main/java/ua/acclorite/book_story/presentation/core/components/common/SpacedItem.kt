package ua.acclorite.book_story.presentation.core.components.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SpacedItem(
    index: Int,
    spacing: Dp,
    content: @Composable () -> Unit
) {
    if (index > 0) Spacer(Modifier.height(spacing))
    content()
}