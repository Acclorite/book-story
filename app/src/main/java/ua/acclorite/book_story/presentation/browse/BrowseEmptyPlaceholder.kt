package ua.acclorite.book_story.presentation.browse

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.placeholder.EmptyPlaceholder
import ua.acclorite.book_story.presentation.core.components.placeholder.ErrorPlaceholder
import ua.acclorite.book_story.ui.browse.BrowseEvent
import ua.acclorite.book_story.ui.theme.Transitions

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BoxScope.BrowseEmptyPlaceholder(
    filesEmpty: Boolean,
    dialogHidden: Boolean,
    isError: Boolean,
    isLoading: Boolean,
    isRefreshing: Boolean,
    storagePermissionState: PermissionState,
    permissionCheck: (BrowseEvent.OnPermissionCheck) -> Unit,
    navigateToHelp: () -> Unit
) {
    AnimatedVisibility(
        visible = isError,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = Transitions.NoExitAnimation
    ) {
        ErrorPlaceholder(
            modifier = Modifier.align(Alignment.Center),
            errorMessage = stringResource(id = R.string.error_permission),
            icon = painterResource(id = R.drawable.error),
            actionTitle = stringResource(id = R.string.grant_permission)
        ) {
            permissionCheck(
                BrowseEvent.OnPermissionCheck(
                    storagePermissionState = storagePermissionState
                )
            )
        }
    }

    AnimatedVisibility(
        visible = !isLoading
                && dialogHidden
                && filesEmpty
                && !isError
                && !isRefreshing,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = Transitions.NoExitAnimation
    ) {
        EmptyPlaceholder(
            message = stringResource(id = R.string.browse_empty),
            icon = painterResource(id = R.drawable.empty_browse),
            actionTitle = stringResource(id = R.string.get_help),
            action = {
                navigateToHelp()
            }
        )
    }
}