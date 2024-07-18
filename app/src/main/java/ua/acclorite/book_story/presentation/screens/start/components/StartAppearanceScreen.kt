package ua.acclorite.book_story.presentation.screens.start.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.subcategories.ThemePreferencesSubcategory

/**
 * Appearance settings.
 */
fun LazyListScope.startAppearanceScreen(
    mainState: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    ThemePreferencesSubcategory(
        state = mainState,
        onMainEvent = onMainEvent,
        title = { stringResource(id = R.string.start_theme_preferences) },
        topPadding = 16.dp,
        bottomPadding = 8.dp
    )
}