package com.acclorite.books_history.presentation.screens.reader.components

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.Category
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.Screen
import com.acclorite.books_history.presentation.screens.library.data.LibraryEvent
import com.acclorite.books_history.presentation.screens.library.data.LibraryViewModel
import com.acclorite.books_history.presentation.screens.reader.data.ReaderEvent
import com.acclorite.books_history.presentation.screens.reader.data.ReaderViewModel

@Composable
fun ReaderEndItem(
    libraryViewModel: LibraryViewModel,
    viewModel: ReaderViewModel,
    navigator: Navigator
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current as ComponentActivity

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(id = R.string.thanks_for_reading, state.book.title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = {
                if (state.book.category != Category.ALREADY_READ) {
                    viewModel.onEvent(
                        ReaderEvent.OnMoveBookToAlreadyRead(
                            refreshList = {
                                libraryViewModel.onEvent(
                                    LibraryEvent.OnLoadList
                                )
                            },
                            updatePage = {
                                libraryViewModel.onEvent(LibraryEvent.OnUpdateCurrentPage(it))
                            },
                            navigator = navigator,
                            context = context
                        )
                    )

                    Toast.makeText(
                        context,
                        context.getString(R.string.book_moved),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.onEvent(
                        ReaderEvent.OnShowSystemBars(
                            context
                        )
                    )
                    navigator.navigate(Screen.LIBRARY)
                }
            },
            contentPadding = PaddingValues(horizontal = 12.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                stringResource(
                    if (state.book.category != Category.ALREADY_READ) R.string.move_to_read
                    else R.string.back_to_library
                ),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}