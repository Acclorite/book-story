package ua.acclorite.book_story.presentation.screens.browse.components.adding_dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithLazyColumn
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel

/**
 * Adding dialog. Adds all selected books to the Library.
 */
@Composable
fun BrowseAddingDialog(
    libraryViewModel: LibraryViewModel,
    viewModel: BrowseViewModel,
    navigator: Navigator
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.add_books),
        imageVectorIcon = Icons.Default.AddChart,
        description = stringResource(id = R.string.add_books_description),
        actionText = stringResource(id = R.string.add),
        isActionEnabled = !state.isBooksLoading &&
                state.selectedBooks.any { it.first is NullableBook.NotNull },
        onDismiss = { viewModel.onEvent(BrowseEvent.OnAddingDialogDismiss) },
        onAction = {
            viewModel.onEvent(
                BrowseEvent.OnAddBooks(
                    navigator,
                    resetScroll = {
                        libraryViewModel.onEvent(LibraryEvent.OnUpdateCurrentPage(0))
                    }
                )
            )
            Toast.makeText(
                context,
                context.getString(R.string.books_added),
                Toast.LENGTH_LONG
            ).show()
        },
        withDivider = true,
        items = {
            if (state.isBooksLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(36.dp)
                        )
                    }
                }
            } else {
                items(state.selectedBooks) { book ->
                    BrowseAddingDialogItem(result = book) {
                        if (it) {
                            viewModel.onEvent(BrowseEvent.OnSelectBook(book))
                        } else {
                            Toast.makeText(
                                context,
                                book.first.message?.asString(context),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    )
}