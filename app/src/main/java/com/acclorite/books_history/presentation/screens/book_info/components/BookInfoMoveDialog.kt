package com.acclorite.books_history.presentation.screens.book_info.components

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
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.Category
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.components.CustomDialogWithLazyColumn
import com.acclorite.books_history.presentation.components.SelectableDialogItem
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoEvent
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoViewModel
import com.acclorite.books_history.presentation.screens.library.data.LibraryEvent
import com.acclorite.books_history.presentation.screens.library.data.LibraryViewModel

@Composable
fun BookInfoMoveDialog(
    libraryViewModel: LibraryViewModel,
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
                        libraryViewModel.onEvent(
                            LibraryEvent.OnLoadList
                        )
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