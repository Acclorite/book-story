package ua.acclorite.book_story.presentation.browse

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.domain.browse.BrowseLayout
import ua.acclorite.book_story.domain.browse.SelectableFile

@Composable
fun BrowseItem(
    layout: BrowseLayout,
    file: SelectableFile,
    hasSelectedFiles: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLongClick: () -> Unit
) {
    when (layout) {
        BrowseLayout.LIST -> {
            BrowseListItem(
                modifier = modifier,
                file = file,
                hasSelectedFiles = hasSelectedFiles,
                onClick = onClick,
                onFavoriteClick = onFavoriteClick,
                onLongClick = onLongClick
            )
        }

        BrowseLayout.GRID -> {
            BrowseGridItem(
                modifier = modifier,
                file = file,
                hasSelectedFiles = hasSelectedFiles,
                onClick = onClick,
                onFavoriteClick = onFavoriteClick,
                onLongClick = onLongClick
            )
        }
    }
}