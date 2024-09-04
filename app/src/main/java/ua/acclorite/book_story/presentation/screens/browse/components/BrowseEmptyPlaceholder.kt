package ua.acclorite.book_story.presentation.screens.browse.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.LocalBrowseViewModel
import ua.acclorite.book_story.presentation.core.components.is_messages.IsEmpty
import ua.acclorite.book_story.presentation.core.components.is_messages.IsError
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.ui.Transitions

/**
 * Browse Empty Placeholder.
 * Shows error or empty message.
 *
 * @param isFilesEmpty Whether the list is empty.
 * @param storagePermissionState Storage [PermissionState].
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BoxScope.BrowseEmptyPlaceholder(
    isFilesEmpty: Boolean,
    storagePermissionState: PermissionState
) {
    val state = LocalBrowseViewModel.current.state
    val onEvent = LocalBrowseViewModel.current.onEvent
    val onNavigate = LocalOnNavigate.current

    CustomAnimatedVisibility(
        visible = state.value.isError,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = Transitions.NoExitAnimation
    ) {
        IsError(
            modifier = Modifier.align(Alignment.Center),
            errorMessage = stringResource(id = R.string.error_permission),
            icon = painterResource(id = R.drawable.error),
            actionTitle = stringResource(id = R.string.grant_permission)
        ) {
            onEvent(
                BrowseEvent.OnPermissionCheck(storagePermissionState)
            )
        }
    }

    CustomAnimatedVisibility(
        visible = !state.value.isLoading
                && isFilesEmpty
                && !state.value.isError
                && !state.value.requestPermissionDialog
                && !state.value.isRefreshing,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = Transitions.NoExitAnimation
    ) {
        IsEmpty(
            message = stringResource(id = R.string.browse_empty),
            icon = painterResource(id = R.drawable.empty_browse),
            actionTitle = stringResource(id = R.string.get_help),
            action = {
                onNavigate {
                    navigate(Screen.Help(false))
                }
            }
        )
    }
}