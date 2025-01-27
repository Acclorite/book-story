package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.book.Book
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.core.components.top_bar.TopAppBar
import ua.acclorite.book_story.presentation.core.components.top_bar.TopAppBarData
import ua.acclorite.book_story.presentation.navigator.NavigatorBackIconButton
import ua.acclorite.book_story.ui.book_info.BookInfoEvent
import ua.acclorite.book_story.ui.theme.DefaultTransition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoTopBar(
    book: Book,
    listState: LazyListState,
    showDetailsBottomSheet: (BookInfoEvent.OnShowDetailsBottomSheet) -> Unit,
    navigateBack: () -> Unit
) {
    val firstVisibleItemIndex = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }

    TopAppBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(0f),
        scrollBehavior = null,
        isTopBarScrolled = listState.canScrollBackward,

        shownTopBar = 0,
        topBars = listOf(
            TopAppBarData(
                contentID = 0,
                contentNavigationIcon = {
                    NavigatorBackIconButton {
                        navigateBack()
                    }
                },
                contentTitle = {
                    DefaultTransition(firstVisibleItemIndex.value > 0) {
                        Text(
                            book.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                },
                contentActions = {
                    IconButton(
                        icon = Icons.Outlined.Info,
                        contentDescription = R.string.file_details,
                        disableOnClick = false,
                        onClick = {
                            showDetailsBottomSheet(BookInfoEvent.OnShowDetailsBottomSheet)
                        }
                    )
                }
            )
        )
    )
}