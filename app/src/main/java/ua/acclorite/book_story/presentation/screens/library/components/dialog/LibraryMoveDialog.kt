package ua.acclorite.book_story.presentation.screens.library.components.dialog

import android.widget.Toast
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithLazyColumn
import ua.acclorite.book_story.presentation.components.custom_dialog.SelectableDialogItem
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryState

/**
 * Move dialog. Moves all selected books to the selected category.
 */
@Composable
fun LibraryMoveDialog(
    state: State<LibraryState>,
    onEvent: (LibraryEvent) -> Unit,
    onHistoryLoadEvent: (HistoryEvent.OnLoadList) -> Unit,
    pagerState: PagerState
) {
    val context = LocalContext.current

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.move_books),
        imageVectorIcon = Icons.AutoMirrored.Outlined.DriveFileMove,
        description = stringResource(
            id = R.string.move_books_description,
            state.value.books.filter { it.second }.size
        ),
        actionText = stringResource(id = R.string.move),
        isActionEnabled = true,
        onDismiss = { onEvent(LibraryEvent.OnShowHideMoveDialog) },
        onAction = {
            onEvent(
                LibraryEvent.OnMoveBooks(
                    pagerState,
                    refreshList = {
                        onHistoryLoadEvent(HistoryEvent.OnLoadList)
                    }
                )
            )
            Toast.makeText(
                context,
                context.getString(R.string.books_moved),
                Toast.LENGTH_LONG
            ).show()
        },
        withDivider = false,
        items = {
            customItems(state.value.categories, key = { it.name }) {
                val category = when (it) {
                    Category.READING -> stringResource(id = R.string.reading_tab)
                    Category.ALREADY_READ -> stringResource(id = R.string.already_read_tab)
                    Category.PLANNING -> stringResource(id = R.string.planning_tab)
                    Category.DROPPED -> stringResource(id = R.string.dropped_tab)
                }

                SelectableDialogItem(
                    selected = it == state.value.selectedCategory,
                    title = category
                ) {
                    onEvent(LibraryEvent.OnSelectCategory(it))
                }
            }
        }
    )
}