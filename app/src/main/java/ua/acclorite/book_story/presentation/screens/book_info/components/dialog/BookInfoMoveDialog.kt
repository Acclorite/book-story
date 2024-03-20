package ua.acclorite.book_story.presentation.screens.book_info.components.dialog

import android.widget.Toast
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithLazyColumn
import ua.acclorite.book_story.presentation.components.custom_dialog.SelectableDialogItem
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel

/**
 * Move dialog. Moves current book to the selected category.
 */
@Composable
fun BookInfoMoveDialog(
    libraryViewModel: LibraryViewModel,
    historyViewModel: HistoryViewModel,
    viewModel: BookInfoViewModel,
    navigator: Navigator
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val categories = remember {
        Category.entries.filter { state.book.category != it }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(BookInfoEvent.OnSelectCategory(categories[0]))
    }

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.move_book),
        imageVectorIcon = Icons.AutoMirrored.Outlined.DriveFileMove,
        description = stringResource(
            id = R.string.move_book_description
        ),
        actionText = stringResource(id = R.string.move),
        isActionEnabled = true,
        onDismiss = { viewModel.onEvent(BookInfoEvent.OnShowHideMoveDialog) },
        onAction = {
            viewModel.onEvent(
                BookInfoEvent.OnMoveBook(
                    refreshList = {
                        libraryViewModel.onEvent(LibraryEvent.OnUpdateBook(it))
                        historyViewModel.onEvent(HistoryEvent.OnUpdateBook(it))
                    },
                    updatePage = {
                        libraryViewModel.onEvent(LibraryEvent.OnUpdateCurrentPage(it))
                    },
                    navigator = navigator
                )
            )
            Toast.makeText(
                context,
                context.getString(R.string.book_moved),
                Toast.LENGTH_LONG
            ).show()
        },
        withDivider = false,
        items = {
            items(categories) {
                val category = when (it) {
                    Category.READING -> stringResource(id = R.string.reading_tab)
                    Category.ALREADY_READ -> stringResource(id = R.string.already_read_tab)
                    Category.PLANNING -> stringResource(id = R.string.planning_tab)
                    Category.DROPPED -> stringResource(id = R.string.dropped_tab)
                }

                SelectableDialogItem(selected = it == state.selectedCategory, title = category) {
                    viewModel.onEvent(BookInfoEvent.OnSelectCategory(it))
                }
            }
        }
    )
}