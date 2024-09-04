package ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseLayout
import ua.acclorite.book_story.presentation.ui.ExpandingTransition

/**
 * Browse Grid Size setting.
 * Lets user specify grid size to be displayed.
 */
@Composable
fun BrowseGridSizeSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    ExpandingTransition(visible = state.value.browseLayout == BrowseLayout.GRID) {
        SliderWithTitle(
            value = state.value.browseGridSize
                    to " ${stringResource(R.string.browse_grid_size_per_row)}",
            valuePlaceholder = stringResource(id = R.string.browse_grid_size_auto),
            showPlaceholder = state.value.browseAutoGridSize,
            fromValue = 0,
            toValue = 10,
            title = stringResource(id = R.string.browse_grid_size_option),
            onValueChange = {
                onMainEvent(MainEvent.OnChangeBrowseAutoGridSize(it == 0))
                onMainEvent(
                    MainEvent.OnChangeBrowseGridSize(it)
                )
            }
        )
    }
}