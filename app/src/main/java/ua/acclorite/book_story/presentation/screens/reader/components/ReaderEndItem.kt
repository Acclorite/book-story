package ua.acclorite.book_story.presentation.screens.reader.components

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Category
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderState

/**
 * Reader end item. Displays at the end of the book.
 *
 * @param state [ReaderState].
 * @param onNavigate Navigator callback.
 * @param onEvent [ReaderEvent] callback.
 * @param onLibraryEvent [LibraryEvent] callback.
 * @param onHistoryUpdateEvent [HistoryEvent] callback.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReaderEndItem(
    state: State<ReaderState>,
    onNavigate: OnNavigate,
    onEvent: (ReaderEvent) -> Unit,
    onLibraryEvent: (LibraryEvent) -> Unit,
    onHistoryUpdateEvent: (HistoryEvent.OnUpdateBook) -> Unit,
) {
    val context = LocalContext.current as ComponentActivity
    val buttonText = remember {
        context.getString(
            if (state.value.book.category != Category.ALREADY_READ) R.string.move_to_read
            else R.string.back_to_library
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                horizontal = 36.dp,
                vertical = 108.dp + WindowInsets.navigationBarsIgnoringVisibility
                    .asPaddingValues()
                    .calculateBottomPadding()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(id = R.string.thanks_for_reading, state.value.book.title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            stringResource(
                id = R.string.letters_and_words,
                state.value.letters,
                state.value.words
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = {
                if (state.value.book.category != Category.ALREADY_READ) {
                    onEvent(
                        ReaderEvent.OnMoveBookToAlreadyRead(
                            onUpdateCategories = {
                                onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                onHistoryUpdateEvent(HistoryEvent.OnUpdateBook(it))
                            },
                            updatePage = {
                                onLibraryEvent(LibraryEvent.OnUpdateCurrentPage(it))
                            },
                            onNavigate = onNavigate,
                            context = context
                        )
                    )

                    Toast.makeText(
                        context,
                        context.getString(R.string.book_moved),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    onEvent(
                        ReaderEvent.OnGoBack(
                            context,
                            refreshList = {
                                onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                onHistoryUpdateEvent(HistoryEvent.OnUpdateBook(it))
                            },
                            navigate = {
                                onNavigate {
                                    navigate(Screen.Library, useBackAnimation = true)
                                }
                            }
                        )
                    )
                }
            },
            contentPadding = PaddingValues(horizontal = 12.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                buttonText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}