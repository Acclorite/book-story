package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomDropDownMenuItem
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel

/**
 * Book info more dropdown. Has three options: Delete, Move and Details.
 */
@Composable
fun BookInfoMoreDropDown(viewModel: BookInfoViewModel, snackbarState: SnackbarHostState) {
    val state by viewModel.state.collectAsState()

    Box {
        IconButton(onClick = {
            viewModel.onEvent(BookInfoEvent.OnShowHideMoreDropDown)
        }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Show drop down",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = state.showMoreDropDown,
            onDismissRequest = {
                viewModel.onEvent(BookInfoEvent.OnShowHideMoreDropDown)
            },
            offset = DpOffset(10.dp, 0.dp)
        ) {
            CustomDropDownMenuItem(
                leadingIcon = Icons.AutoMirrored.Outlined.DriveFileMove,
                text = stringResource(id = R.string.move_this_book)
            ) {
                viewModel.onEvent(BookInfoEvent.OnShowHideMoveDialog)
                viewModel.onEvent(BookInfoEvent.OnShowHideMoreDropDown)
            }
            CustomDropDownMenuItem(
                leadingIcon = Icons.Outlined.Delete,
                text = stringResource(id = R.string.delete_this_book)
            ) {
                viewModel.onEvent(BookInfoEvent.OnShowHideDeleteDialog)
                viewModel.onEvent(BookInfoEvent.OnShowHideMoreDropDown)
            }
            CustomDropDownMenuItem(
                leadingIcon = Icons.Outlined.Info,
                text = stringResource(id = R.string.details_book)
            ) {
                snackbarState.currentSnackbarData?.dismiss()
                viewModel.onEvent(BookInfoEvent.OnShowHideDetailsBottomSheet)
                viewModel.onEvent(BookInfoEvent.OnShowHideMoreDropDown)
            }
        }
    }
}