package ua.acclorite.book_story.presentation.settings.browse.general.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.browse.BrowseLayout
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.core.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun BrowseLayoutOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.browse_layout_option),
        buttons = BrowseLayout.entries.map {
            ButtonItem(
                it.toString(),
                when (it) {
                    BrowseLayout.LIST -> stringResource(id = R.string.browse_layout_list)
                    BrowseLayout.GRID -> stringResource(id = R.string.browse_layout_grid)
                },
                MaterialTheme.typography.labelLarge,
                it == state.value.browseLayout
            )
        }
    ) {
        mainModel.onEvent(
            MainEvent.OnChangeBrowseLayout(
                it.id
            )
        )
    }
}