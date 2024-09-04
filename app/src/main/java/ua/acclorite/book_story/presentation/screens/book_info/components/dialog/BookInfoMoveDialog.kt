package ua.acclorite.book_story.presentation.screens.book_info.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.presentation.core.components.LocalBookInfoViewModel
import ua.acclorite.book_story.presentation.core.components.LocalHistoryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalLibraryViewModel
import ua.acclorite.book_story.presentation.core.components.customItems
import ua.acclorite.book_story.presentation.core.components.custom_dialog.CustomDialogWithLazyColumn
import ua.acclorite.book_story.presentation.core.components.custom_dialog.SelectableDialogItem
import ua.acclorite.book_story.presentation.core.navigation.LocalOnNavigate
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent

/**
 * Move dialog.
 * Moves current book to the selected category.
 */
@Composable
fun BookInfoMoveDialog() {
    val state = LocalBookInfoViewModel.current.state
    val onEvent = LocalBookInfoViewModel.current.onEvent
    val onLibraryEvent = LocalLibraryViewModel.current.onEvent
    val onHistoryEvent = LocalHistoryViewModel.current.onEvent
    val onNavigate = LocalOnNavigate.current
    val context = LocalContext.current

    val categories = remember {
        Category.entries.filter { state.value.book.category != it }
    }

    LaunchedEffect(Unit) {
        onEvent(BookInfoEvent.OnSelectCategory(categories[0]))
    }

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.move_book),
        imageVectorIcon = Icons.AutoMirrored.Outlined.DriveFileMove,
        description = stringResource(
            id = R.string.move_book_description
        ),
        actionText = stringResource(id = R.string.move),
        isActionEnabled = true,
        onDismiss = { onEvent(BookInfoEvent.OnShowHideMoveDialog) },
        onAction = {
            onEvent(
                BookInfoEvent.OnMoveBook(
                    refreshList = {
                        onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                        onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                    },
                    updatePage = {
                        onLibraryEvent(LibraryEvent.OnUpdateCurrentPage(it))
                    },
                    onNavigate = onNavigate
                )
            )
            context.getString(R.string.book_moved)
                .showToast(context = context)
        },
        withDivider = false,
        items = {
            customItems(categories, key = { it.name }) {
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
                    onEvent(BookInfoEvent.OnSelectCategory(it))
                }
            }
        }
    )
}