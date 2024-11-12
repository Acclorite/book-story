package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.Position
import ua.acclorite.book_story.presentation.core.components.modal_bottom_sheet.ModalBottomSheet
import ua.acclorite.book_story.presentation.core.navigation.NavigationItem
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel

/**
 * Book Info More Bottom Sheet.
 * Replaces DropDown.
 *
 * @param snackbarState [SnackbarHostState].
 */
@Composable
fun BookInfoMoreBottomSheet(snackbarState: SnackbarHostState) {
    val onEvent = BookInfoViewModel.getEvent()

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            onEvent(BookInfoEvent.OnShowHideMoreBottomSheet(false))
        },
        sheetGesturesEnabled = true
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                NavigationItem(
                    title = stringResource(id = R.string.details_book),
                    primary = false,
                    position = Position.SOLO
                ) {
                    snackbarState.currentSnackbarData?.dismiss()
                    onEvent(BookInfoEvent.OnShowHideDetailsBottomSheet)
                }
            }

            item {
                Spacer(Modifier.height(18.dp))
            }

            item {
                NavigationItem(
                    title = stringResource(id = R.string.delete_this_book),
                    primary = true,
                    position = Position.TOP
                ) {
                    onEvent(BookInfoEvent.OnShowHideDeleteDialog)
                }
            }

            item {
                NavigationItem(
                    title = stringResource(id = R.string.move_this_book),
                    primary = true,
                    position = Position.BOTTOM
                ) {
                    onEvent(BookInfoEvent.OnShowHideMoveDialog)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}