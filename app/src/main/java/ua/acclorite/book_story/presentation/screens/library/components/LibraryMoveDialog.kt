package ua.acclorite.book_story.presentation.screens.library.components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.presentation.components.CustomDialogWithLazyColumn
import ua.acclorite.book_story.presentation.components.SelectableDialogItem
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryMoveDialog(viewModel: LibraryViewModel, pagerState: PagerState) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.move_books),
        imageVectorIcon = Icons.AutoMirrored.Outlined.DriveFileMove,
        description = stringResource(
            id = R.string.move_books_description,
            state.books.filter { it.second }.size
        ),
        actionText = stringResource(id = R.string.move),
        isActionEnabled = true,
        onDismiss = { viewModel.onEvent(LibraryEvent.OnShowHideMoveDialog) },
        onAction = {
            viewModel.onEvent(LibraryEvent.OnMoveBooks(pagerState))
            Toast.makeText(
                context,
                context.getString(R.string.books_moved),
                Toast.LENGTH_LONG
            ).show()
        },
        withDivider = false,
        items = {
            items(state.categories) {
                val category = when (it) {
                    Category.READING -> stringResource(id = R.string.reading_tab)
                    Category.ALREADY_READ -> stringResource(id = R.string.already_read_tab)
                    Category.PLANNING -> stringResource(id = R.string.planning_tab)
                    Category.DROPPED -> stringResource(id = R.string.dropped_tab)
                }

                SelectableDialogItem(selected = it == state.selectedCategory, title = category) {
                    viewModel.onEvent(LibraryEvent.OnSelectCategory(it))
                }
            }
        }
    )
}