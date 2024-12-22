package ua.acclorite.book_story.presentation.history

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.placeholder.EmptyPlaceholder
import ua.acclorite.book_story.ui.theme.Transitions

@Composable
fun BoxScope.HistoryEmptyPlaceholder(
    isLoading: Boolean,
    isRefreshing: Boolean,
    isHistoryEmpty: Boolean
) {
    AnimatedVisibility(
        visible = !isLoading
                && isHistoryEmpty
                && !isRefreshing,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = fadeOut(tween(0))
    ) {
        EmptyPlaceholder(
            modifier = Modifier.align(Alignment.Center),
            message = stringResource(id = R.string.history_empty),
            icon = painterResource(id = R.drawable.empty_history)
        )
    }
}