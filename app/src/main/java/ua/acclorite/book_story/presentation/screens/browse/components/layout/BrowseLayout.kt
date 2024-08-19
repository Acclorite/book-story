package ua.acclorite.book_story.presentation.screens.browse.components.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.browse.components.layout.grid.BrowseGridLayout
import ua.acclorite.book_story.presentation.screens.browse.components.layout.list.BrowseListLayout
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseState
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseLayout

/**
 * Browse Layout.
 * Shows Grid or List.
 *
 * @param state [BrowseState].
 * @param mainState [MainState].
 * @param filteredFiles Filtered items.
 * @param onLongItemClick OnLongItemClick callback.
 * @param onFavoriteItemClick OnFavoriteItemClick callback.
 * @param onItemClick OnItemClick callback.
 */
@Composable
fun BrowseLayout(
    state: State<BrowseState>,
    mainState: State<MainState>,
    filteredFiles: List<SelectableFile>,
    onLongItemClick: (SelectableFile) -> Unit,
    onFavoriteItemClick: (SelectableFile) -> Unit,
    onItemClick: (SelectableFile) -> Unit
) {
    when (mainState.value.browseLayout!!) {
        BrowseLayout.LIST -> {
            BrowseListLayout(
                state = state,
                filteredFiles = filteredFiles,
                onLongItemClick = onLongItemClick,
                onFavoriteItemClick = onFavoriteItemClick,
                onItemClick = onItemClick
            )
        }

        BrowseLayout.GRID -> {
            BrowseGridLayout(
                state = state,
                gridSize = mainState.value.browseGridSize!!,
                autoGridSize = mainState.value.browseAutoGridSize!!,
                filteredFiles = filteredFiles,
                onLongItemClick = onLongItemClick,
                onFavoriteItemClick = onFavoriteItemClick,
                onItemClick = onItemClick
            )
        }
    }
}