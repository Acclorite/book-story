package ua.acclorite.book_story.presentation.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.placeholder.EmptyPlaceholder
import ua.acclorite.book_story.ui.theme.Transitions

@Composable
fun BoxScope.LibraryEmptyPlaceholder(
    isLoading: Boolean,
    isRefreshing: Boolean,
    isBooksEmpty: Boolean,
    navigateToBrowse: () -> Unit
) {
    AnimatedVisibility(
        visible = !isLoading
                && !isRefreshing
                && isBooksEmpty,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = fadeOut(tween(0))
    ) {
        EmptyPlaceholder(
            message = stringResource(id = R.string.library_empty),
            icon = painterResource(id = R.drawable.empty_library),
            modifier = Modifier.align(Alignment.Center),
            actionTitle = stringResource(id = R.string.add_book)
        ) {
            navigateToBrowse()
        }
    }
}