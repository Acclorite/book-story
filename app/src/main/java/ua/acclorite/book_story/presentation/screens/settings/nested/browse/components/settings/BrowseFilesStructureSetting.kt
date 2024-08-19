package ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.settings

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.components.SegmentedButtonWithTitle
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseFilesStructure

/**
 * Browse Files Structure setting.
 * Lets user choose between all and folders structure for Browse.
 */
@Composable
fun LazyItemScope.BrowseFilesStructureSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.browse_files_structure_option),
        modifier = Modifier.animateItem(),
        buttons = BrowseFilesStructure.entries.map {
            ButtonItem(
                it.toString(),
                when (it) {
                    BrowseFilesStructure.ALL_FILES -> stringResource(id = R.string.files_structure_all)
                    BrowseFilesStructure.DIRECTORIES -> stringResource(id = R.string.files_structure_directory)
                },
                MaterialTheme.typography.labelLarge,
                it == state.value.browseFilesStructure
            )
        }
    ) {
        onMainEvent(
            MainEvent.OnChangeBrowseFilesStructure(
                it.id
            )
        )
    }
}