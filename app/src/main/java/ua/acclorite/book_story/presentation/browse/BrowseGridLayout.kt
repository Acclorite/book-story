package ua.acclorite.book_story.presentation.browse

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.browse.BrowseLayout
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.presentation.core.components.common.LazyVerticalGridWithScrollbar
import ua.acclorite.book_story.presentation.core.components.common.header
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.providePrimaryScrollbar

@Composable
fun BrowseGridLayout(
    gridSize: Int,
    autoGridSize: Boolean,
    gridState: LazyGridState,
    files: List<SelectableFile>,
    hasSelectedFiles: Boolean,
    onLongItemClick: (SelectableFile) -> Unit,
    onFavoriteItemClick: (SelectableFile) -> Unit,
    onItemClick: (SelectableFile) -> Unit,
) {
    LazyVerticalGridWithScrollbar(
        columns = if (autoGridSize) GridCells.Adaptive(170.dp)
        else GridCells.Fixed(gridSize.coerceAtLeast(1)),
        state = gridState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        scrollbarSettings = Constants.providePrimaryScrollbar(false)
    ) {
        header {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(
            files,
            key = { it.fileOrDirectory.path }
        ) { selectableFile ->
            BrowseItem(
                file = selectableFile,
                modifier = Modifier.animateItem(),
                layout = BrowseLayout.GRID,
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

        header {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}