package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.CustomDropDownMenuItem
import ua.acclorite.book_story.presentation.core.components.CustomIconButton
import ua.acclorite.book_story.presentation.core.components.LocalBookInfoViewModel
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent

/**
 * Book info more dropdown. Has three options: Delete, Move and Details.
 */
@Composable
fun BookInfoMoreDropDown(snackbarState: SnackbarHostState) {
    val state = LocalBookInfoViewModel.current.state
    val onEvent = LocalBookInfoViewModel.current.onEvent

    var showDropDown by remember { mutableStateOf(false) }

    Box {
        CustomIconButton(
            icon = Icons.Outlined.MoreVert,
            contentDescription = R.string.show_dropdown_content_desc,
            disableOnClick = false,
            enabled = !showDropDown &&
                    !state.value.isRefreshing
        ) {
            showDropDown = true
        }

        DropdownMenu(
            expanded = showDropDown,
            onDismissRequest = {
                showDropDown = false
            },
            offset = DpOffset((-15).dp, 0.dp)
        ) {
            CustomDropDownMenuItem(
                leadingIcon = Icons.AutoMirrored.Outlined.DriveFileMove,
                text = stringResource(id = R.string.move_this_book)
            ) {
                onEvent(BookInfoEvent.OnShowHideMoveDialog)
                showDropDown = false
            }
            CustomDropDownMenuItem(
                leadingIcon = Icons.Outlined.Delete,
                text = stringResource(id = R.string.delete_this_book)
            ) {
                onEvent(BookInfoEvent.OnShowHideDeleteDialog)
                showDropDown = false
            }
            CustomDropDownMenuItem(
                leadingIcon = Icons.Outlined.Info,
                text = stringResource(id = R.string.details_book)
            ) {
                snackbarState.currentSnackbarData?.dismiss()
                onEvent(BookInfoEvent.OnShowHideDetailsBottomSheet)
                showDropDown = false
            }
        }
    }
}