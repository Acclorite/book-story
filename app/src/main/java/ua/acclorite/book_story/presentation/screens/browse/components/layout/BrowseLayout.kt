package ua.acclorite.book_story.presentation.screens.browse.components.layout

import androidx.compose.runtime.Composable
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.screens.browse.components.layout.grid.BrowseGridLayout
import ua.acclorite.book_story.presentation.screens.browse.components.layout.list.BrowseListLayout
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseLayout

/**
 * Browse Layout.
 * Shows Grid or List.
 *
 * @param filteredFiles Filtered items.
 * @param onLongItemClick OnLongItemClick callback.
 * @param onFavoriteItemClick OnFavoriteItemClick callback.
 * @param onItemClick OnItemClick callback.
 */
@Composable
fun BrowseLayout(
    filteredFiles: List<SelectableFile>,
    onLongItemClick: (SelectableFile) -> Unit,
    onFavoriteItemClick: (SelectableFile) -> Unit,
    onItemClick: (SelectableFile) -> Unit
) {
    val mainState = LocalMainViewModel.current.state

    when (mainState.value.browseLayout) {
        BrowseLayout.LIST -> {
            BrowseListLayout(
                filteredFiles = filteredFiles,
                onLongItemClick = onLongItemClick,
                onFavoriteItemClick = onFavoriteItemClick,
                onItemClick = onItemClick
            )
        }

        BrowseLayout.GRID -> {
            BrowseGridLayout(
                gridSize = mainState.value.browseGridSize,
                autoGridSize = mainState.value.browseAutoGridSize,
                filteredFiles = filteredFiles,
                onLongItemClick = onLongItemClick,
                onFavoriteItemClick = onFavoriteItemClick,
                onItemClick = onItemClick
            )
        }
    }
}