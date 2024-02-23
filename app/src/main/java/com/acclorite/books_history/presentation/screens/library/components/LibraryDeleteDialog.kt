package com.acclorite.books_history.presentation.screens.library.components

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.components.CustomDialogWithContent
import com.acclorite.books_history.presentation.screens.library.data.LibraryEvent
import com.acclorite.books_history.presentation.screens.library.data.LibraryViewModel

@Composable
fun LibraryDeleteDialog(viewModel: LibraryViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    CustomDialogWithContent(
        title = stringResource(id = R.string.delete_books),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_books_description,
            state.books.filter { it.second }.size
        ),
        actionText = stringResource(id = R.string.delete),
        onDismiss = { viewModel.onEvent(LibraryEvent.OnShowHideDeleteDialog) },
        withDivider = false,
        isActionEnabled = true,
        onAction = {
            viewModel.onEvent(LibraryEvent.OnDeleteBooks)
            Toast.makeText(
                context,
                context.getString(R.string.books_deleted),
                Toast.LENGTH_LONG
            ).show()
        }
    )
}