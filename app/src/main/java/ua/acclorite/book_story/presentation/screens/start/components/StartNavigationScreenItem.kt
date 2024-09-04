package ua.acclorite.book_story.presentation.screens.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.presentation.core.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.CustomLazyColumn
import ua.acclorite.book_story.presentation.core.components.LocalStartViewModel
import ua.acclorite.book_story.presentation.screens.start.data.StartNavigationScreen
import ua.acclorite.book_story.presentation.ui.Transitions

/**
 * Navigation item screen. Uses sliding anim.
 */
@Composable
fun StartNavigationScreenItem(
    screen: StartNavigationScreen,
    content: LazyListScope.() -> Unit
) {
    val state = LocalStartViewModel.current.state
    val listState = rememberLazyListState()

    CustomAnimatedVisibility(
        visible = state.value.currentScreen == screen,
        enter = if (state.value.useBackAnimation) Transitions.BackSlidingTransitionIn
        else Transitions.SlidingTransitionIn,
        exit = if (state.value.useBackAnimation) Transitions.BackSlidingTransitionOut
        else Transitions.SlidingTransitionOut
    ) {
        CustomLazyColumn(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            state = listState
        ) {
            content()
        }
    }
}
