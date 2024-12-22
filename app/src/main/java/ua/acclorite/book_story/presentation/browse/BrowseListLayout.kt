package ua.acclorite.book_story.presentation.browse

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.browse.BrowseLayout
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.providePrimaryScrollbar

@Composable
fun BrowseListLayout(
    files: List<SelectableFile>,
    hasSelectedFiles: Boolean,
    listState: LazyListState,
    onLongItemClick: (SelectableFile) -> Unit,
    onFavoriteItemClick: (SelectableFile) -> Unit,
    onItemClick: (SelectableFile) -> Unit,
) {
    LazyColumnWithScrollbar(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        scrollbarSettings = Constants.providePrimaryScrollbar(false)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(
            files,
            key = { it.fileOrDirectory.path }
        ) { selectableFile ->
            BrowseItem(
                file = selectableFile,
                layout = BrowseLayout.LIST,
                modifier = Modifier.animateItem(),
                hasSelectedFiles = hasSelectedFiles,
                onLongClick = {
                    onLongItemClick(selectableFile)
                },
                onFavoriteClick = {
                    onFavoriteItemClick(selectableFile)
                },
                onClick = {
                    onItemClick(selectableFile)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}