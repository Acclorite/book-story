package ua.acclorite.book_story.presentation.screens.reader.components.app_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.removeDigits
import ua.acclorite.book_story.presentation.data.removeTrailingZero
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel

/**
 * Reader bottom bar. Has a slider to change progress.
 */
@Composable
fun ReaderBottomBar(
    viewModel: ReaderViewModel,
    libraryViewModel: LibraryViewModel,
    navigator: Navigator,
    scrollState: LazyListState,
    systemBarsColor: Color
) {
    val state by viewModel.state.collectAsState()
    val book = state.book

    Column(
        Modifier
            .fillMaxWidth()
            .background(systemBarsColor)
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = {}
            )
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text =
            (book.progress * 100)
                .toDouble()
                .removeDigits(4)
                .removeTrailingZero()
                .dropWhile { it == '-' } + "%",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )
        Slider(
            value = book.progress,
            onValueChange = {
                viewModel.onEvent(
                    ReaderEvent.OnChangeProgress(
                        progress = it,
                        navigator = navigator,
                        refreshList = { book ->
                            libraryViewModel.onEvent(
                                LibraryEvent.OnUpdateBook(
                                    book
                                )
                            )
                        }
                    )
                )
                viewModel.onEvent(ReaderEvent.OnScroll(scrollState, it))
            },
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primary,
                thumbColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.15f),
                disabledActiveTickColor = Color.Transparent,
                disabledActiveTrackColor = Color.Transparent,
                disabledInactiveTickColor = Color.Transparent,
                disabledInactiveTrackColor = Color.Transparent,
                disabledThumbColor = Color.Transparent,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            )
        )
    }
}