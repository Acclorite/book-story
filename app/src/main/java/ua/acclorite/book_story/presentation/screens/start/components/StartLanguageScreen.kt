package ua.acclorite.book_story.presentation.screens.start.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.components.custom_dialog.SelectableDialogItem
import ua.acclorite.book_story.presentation.data.MainEvent

/**
 * Language settings.
 */
fun LazyListScope.startLanguageScreen(
    onMainEvent: (MainEvent) -> Unit,
    languages: List<ButtonItem>
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        CategoryTitle(
            title = stringResource(id = R.string.start_language_preferences),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    customItems(languages, key = { it.id }) {
        SelectableDialogItem(
            selected = it.selected,
            title = it.title,
            horizontalPadding = 18.dp
        ) {
            onMainEvent(MainEvent.OnChangeLanguage(it.id))
        }
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
    }
}