package ua.acclorite.book_story.presentation.screens.browse.components.adding_dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.NullableBook
import ua.acclorite.book_story.presentation.core.components.dialog.DialogWithLazyColumn
import ua.acclorite.book_story.presentation.core.components.progress_indicator.CircularProgressIndicator
import ua.acclorite.book_story.presentation.core.navigation.LocalNavigator
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import java.util.UUID

/**
 * Adding dialog.
 * Adds all selected books to the Library.
 */
@Composable
fun BrowseAddingDialog() {
    val context = LocalContext.current
    val state = BrowseViewModel.getState()
    val onEvent = BrowseViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onNavigate = LocalNavigator.current

    DialogWithLazyColumn(
        title = stringResource(id = R.string.add_books),
        imageVectorIcon = Icons.Default.AddChart,
        description = stringResource(id = R.string.add_books_description),
        actionText = stringResource(id = R.string.add),
        isActionEnabled = !state.value.isBooksLoading &&
                state.value.selectedBooks.any { it.first is NullableBook.NotNull },
        onDismiss = { onEvent(BrowseEvent.OnAddingDialogDismiss) },
        onAction = {
            onEvent(
                BrowseEvent.OnAddBooks(
                    onNavigate = onNavigate,
                    resetScroll = {
                        onLibraryEvent(LibraryEvent.OnUpdateCurrentPage(0))
                        onLibraryEvent(LibraryEvent.OnLoadList)
                    },
                    onFailed = {
                        context.getString(R.string.error_something_went_wrong)
                            .showToast(context = context)
                    },
                    onSuccess = {
                        context.getString(R.string.books_added)
                            .showToast(context = context)
                    }
                )
            )
        },
        withDivider = true,
        items = {
            if (state.value.isBooksLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(36.dp)
                        )
                    }
                }
            } else {
                items(
                    state.value.selectedBooks,
                    key = { it.first.fileName ?: UUID.randomUUID() }
                ) { book ->
                    BrowseAddingDialogItem(
                        result = book
                    ) {
                        if (it) {
                            onEvent(BrowseEvent.OnSelectBook(book))
                        } else {
                            book.first.message?.asString(context)
                                ?.showToast(context = context)
                        }
                    }
                }
            }
        }
    )
}