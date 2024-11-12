package ua.acclorite.book_story.presentation.core.components.modal_bottom_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * Bottom Sheet.
 * Fixes bugs with [ModalBottomSheet].
 *
 * @param modifier [Modifier].
 * @param hasFixedHeight Whether the bottom sheet has fixed height passed through [modifier] or not.
 * @param scrimColor Scrim color.
 * @param shape Shape.
 * @param containerColor Container color.
 * @param onDismissRequest OnDismiss callback.
 * @param sheetGesturesEnabled Whether bottom sheet gestures are enabled.
 * @param dragHandle Drag Handle, pass null to disable.
 * @param content Content inside [ModalBottomSheet].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
    modifier: Modifier = Modifier,
    hasFixedHeight: Boolean = false,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    onDismissRequest: () -> Unit,
    sheetGesturesEnabled: Boolean,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    content: @Composable ColumnScope.() -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        ModalBottomSheet(
            scrimColor = scrimColor,
            dragHandle = dragHandle,
            modifier = Modifier
                .then(
                    if (hasFixedHeight) Modifier.align(Alignment.BottomCenter)
                    else Modifier
                )
                .fillMaxWidth()
                .then(modifier),
            sheetGesturesEnabled = sheetGesturesEnabled,
            onDismissRequest = {
                onDismissRequest()
            },
            shape = shape,
            sheetState = rememberModalBottomSheetState(true),
            containerColor = containerColor
        ) {
            content()
        }
    }
}