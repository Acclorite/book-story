package ua.acclorite.book_story.presentation.settings.reader.reading_speed.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.settings.SwitchWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun PerceptionExpanderOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SwitchWithTitle(
        selected = state.value.perceptionExpander,
        title = stringResource(id = R.string.perception_expander_option),
        description = stringResource(id = R.string.perception_expander_option_desc),
        onClick = {
            mainModel.onEvent(
                MainEvent.OnChangePerceptionExpander(
                    !state.value.perceptionExpander
                )
            )
        }
    )
}