package ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseFilesStructure
import ua.acclorite.book_story.presentation.ui.ExpandingTransition

/**
 * Browse Pin Favorite Directories setting.
 * If true, shows all favorite directories in root directory and moves them to the top in nested directories.
 */
@Composable
fun BrowsePinFavoriteDirectoriesSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    ExpandingTransition(visible = state.value.browseFilesStructure == BrowseFilesStructure.DIRECTORIES) {
        SwitchWithTitle(
            selected = state.value.browsePinFavoriteDirectories,
            title = stringResource(id = R.string.browse_pin_favorite_directories_option),
            description = stringResource(id = R.string.browse_pin_favorite_directories_option_desc)
        ) {
            onMainEvent(
                MainEvent.OnChangeBrowsePinFavoriteDirectories(
                    !state.value.browsePinFavoriteDirectories
                )
            )
        }
    }
}