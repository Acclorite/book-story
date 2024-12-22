package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.presentation.core.components.dialog.Dialog
import ua.acclorite.book_story.presentation.core.components.dialog.SelectableDialogItem
import ua.acclorite.book_story.ui.book_info.BookInfoEvent

@Composable
fun BookInfoMoveDialog(
    book: Book,
    actionMoveDialog: (BookInfoEvent.OnActionMoveDialog) -> Unit,
    dismissDialog: (BookInfoEvent.OnDismissDialog) -> Unit,
    navigateToLibrary: () -> Unit
) {
    val context = LocalContext.current

    val categories = remember {
        Category.entries.filter { book.category != it }
    }
    val selectedCategory = remember {
        mutableStateOf(categories[0])
    }

    Dialog(
        title = stringResource(id = R.string.move_book),
        imageVectorIcon = Icons.AutoMirrored.Outlined.DriveFileMove,
        description = stringResource(
            id = R.string.move_book_description
        ),
        actionText = stringResource(id = R.string.move),
        actionEnabled = true,
        onDismiss = { dismissDialog(BookInfoEvent.OnDismissDialog) },
        onAction = {
            actionMoveDialog(
                BookInfoEvent.OnActionMoveDialog(
                    category = selectedCategory.value,
                    context = context,
                    navigateToLibrary = navigateToLibrary
                )
            )
        },
        withDivider = false,
        items = {
            items(categories, key = { it.name }) {
                val category = when (it) {
                    Category.READING -> stringResource(id = R.string.reading_tab)
                    Category.ALREADY_READ -> stringResource(id = R.string.already_read_tab)
                    Category.PLANNING -> stringResource(id = R.string.planning_tab)
                    Category.DROPPED -> stringResource(id = R.string.dropped_tab)
                }

                SelectableDialogItem(
                    selected = it == selectedCategory.value,
                    title = category
                ) {
                    selectedCategory.value = it
                }
            }
        }
    )
}