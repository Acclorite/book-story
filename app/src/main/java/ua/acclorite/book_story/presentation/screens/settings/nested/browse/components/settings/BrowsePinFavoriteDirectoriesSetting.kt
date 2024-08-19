package ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.settings

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseFilesStructure
import ua.acclorite.book_story.presentation.ui.SlidingTransition

/**
 * Browse Pin Favorite Directories setting.
 * If true, shows all favorite directories in root directory and moves them to the top in nested directories.
 */
@Composable
fun LazyItemScope.BrowsePinFavoriteDirectoriesSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SlidingTransition(
        modifier = Modifier.animateItem(
            fadeInSpec = null,
            fadeOutSpec = null
        ),
        visible = state.value.browseFilesStructure == BrowseFilesStructure.DIRECTORIES
    ) {
        SwitchWithTitle(
            selected = state.value.browsePinFavoriteDirectories!!,
            title = stringResource(id = R.string.browse_pin_favorite_directories_option),
            description = stringResource(id = R.string.browse_pin_favorite_directories_option_desc)
        ) {
            onMainEvent(
                MainEvent.OnChangeBrowsePinFavoriteDirectories(
                    !state.value.browsePinFavoriteDirectories!!
                )
            )
        }
    }
}