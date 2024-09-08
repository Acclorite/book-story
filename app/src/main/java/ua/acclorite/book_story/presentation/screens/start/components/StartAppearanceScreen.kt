package ua.acclorite.book_story.presentation.screens.start.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.subcategories.ThemePreferencesSubcategory

/**
 * Appearance settings.
 */
fun LazyListScope.startAppearanceScreen() {
    ThemePreferencesSubcategory(
        title = { stringResource(id = R.string.start_theme_preferences) },
        showDivider = false,
        topPadding = 16.dp,
        bottomPadding = 8.dp
    )
}