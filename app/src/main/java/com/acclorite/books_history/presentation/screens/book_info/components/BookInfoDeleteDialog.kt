package com.acclorite.books_history.presentation.screens.book_info.components

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.components.CustomDialogWithContent
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoEvent
import com.acclorite.books_history.presentation.screens.book_info.data.BookInfoViewModel
import com.acclorite.books_history.presentation.screens.library.data.LibraryEvent
import com.acclorite.books_history.presentation.screens.library.data.LibraryViewModel

@Composable
fun BookInfoDeleteDialog(
    libraryViewModel: LibraryViewModel,
    viewModel: BookInfoViewModel,
    navigator: Navigator
) {
    val context = LocalContext.current

    CustomDialogWithContent(
        title = stringResource(id = R.string.delete_book),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_book_description
        ),
        actionText = stringResource(id = R.string.delete),
        onDismiss = { viewModel.onEvent(BookInfoEvent.OnShowHideDeleteDialog) },
        withDivider = false,
        isActionEnabled = true,
        onAction = {
            viewModel.onEvent(
                BookInfoEvent.OnDeleteBook(
                    navigator = navigator,
                    refreshList = {
                        libraryViewModel.onEvent(
                            LibraryEvent.OnLoadList
                        )
                    }
                )
            )
            Toast.makeText(
                context,
                context.getString(R.string.book_deleted),
                Toast.LENGTH_LONG
            ).show()
        }
    )
}