package ua.acclorite.book_story.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomSnackbar(snackbarState: SnackbarHostState) {
    val dismissState = rememberDismissState { value ->
        if (value != DismissValue.Default) {
            snackbarState.currentSnackbarData?.dismiss()
            true
        } else false
    }

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue != DismissValue.Default) {
            dismissState.reset()
        }
    }

    SwipeToDismiss(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        state = dismissState,
        background = {}
    ) {
        SnackbarHost(
            hostState = snackbarState,
            snackbar = { data ->
                Snackbar(
                    data
                )
            }
        )
    }
}