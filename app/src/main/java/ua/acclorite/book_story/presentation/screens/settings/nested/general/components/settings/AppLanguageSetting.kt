package ua.acclorite.book_story.presentation.screens.settings.nested.general.components.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.core.components.LocalMainViewModel
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.screens.settings.components.ChipsWithTitle

/**
 * App Language setting.
 * Changes app's language.
 */
@Composable
fun AppLanguageSetting() {
    val state = LocalMainViewModel.current.state
    val onMainEvent = LocalMainViewModel.current.onEvent

    ChipsWithTitle(
        title = stringResource(id = R.string.language_option),
        chips = Constants.LANGUAGES.sortedBy { it.second }.map {
            ButtonItem(
                it.first,
                it.second,
                MaterialTheme.typography.labelLarge,
                it.first == state.value.language
            )
        }.sortedBy { it.title }
    ) {
        onMainEvent(
            MainEvent.OnChangeLanguage(
                it.id
            )
        )
    }
}