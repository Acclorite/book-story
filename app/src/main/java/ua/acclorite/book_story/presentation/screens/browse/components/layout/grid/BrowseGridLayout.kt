package ua.acclorite.book_story.presentation.screens.browse.components.layout.grid

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.components.header
import ua.acclorite.book_story.presentation.screens.browse.components.layout.BrowseItem
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseState
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseLayout

@Composable
fun BrowseGridLayout(
    state: State<BrowseState>,
    gridSize: Int,
    autoGridSize: Boolean,
    filteredFiles: List<SelectableFile>,
    onLongItemClick: (SelectableFile) -> Unit,
    onFavoriteItemClick: (SelectableFile) -> Unit,
    onItemClick: (SelectableFile) -> Unit,
) {
    LazyVerticalGrid(
        columns = if (autoGridSize) GridCells.Adaptive(170.dp)
        else GridCells.Fixed(gridSize.coerceAtLeast(1)),
        state = state.value.gridState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        header {
            Spacer(modifier = Modifier.height(8.dp))
        }

        customItems(
            filteredFiles,
            key = { it.fileOrDirectory.path }
        ) { selectableFile ->
            BrowseItem(
                file = selectableFile,
                modifier = Modifier.animateItem(),
                layout = BrowseLayout.GRID,
                hasSelectedFiles = state.value.selectableFiles.any { it.isSelected },
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