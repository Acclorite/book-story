package ua.acclorite.book_story.presentation.settings.browse.general.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.browse.BrowseFilesStructure
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.core.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun FilesStructureOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.browse_files_structure_option),
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
        mainModel.onEvent(
            MainEvent.OnChangeBrowseFilesStructure(
                it.id
            )
        )
    }
}