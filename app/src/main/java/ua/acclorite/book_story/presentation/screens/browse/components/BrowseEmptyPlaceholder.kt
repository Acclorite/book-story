package ua.acclorite.book_story.presentation.screens.browse.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.is_messages.IsEmpty
import ua.acclorite.book_story.presentation.components.is_messages.IsError
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseState
import ua.acclorite.book_story.presentation.ui.Transitions

/**
 * Browse Empty Placeholder.
 * Shows error or empty message.
 *
 * @param state [BrowseState].
 * @param isFilesEmpty Whether the list is empty.
 * @param storagePermissionState Storage [PermissionState].
 * @param onNavigate Navigator callback.
 * @param onEvent [BrowseEvent] callback.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BoxScope.BrowseEmptyPlaceholder(
    state: State<BrowseState>,
    isFilesEmpty: Boolean,
    storagePermissionState: PermissionState,
    onNavigate: OnNavigate,
    onEvent: (BrowseEvent) -> Unit
) {
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