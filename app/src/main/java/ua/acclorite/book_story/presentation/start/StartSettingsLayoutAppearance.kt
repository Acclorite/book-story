@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.start

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.appearance.theme_preferences.ThemePreferencesSubcategory

fun LazyListScope.StartSettingsLayoutAppearance() {
    ThemePreferencesSubcategory(
        title = { stringResource(id = R.string.start_theme_preferences) },
        showDivider = false,
        topPadding = 16.dp,
        bottomPadding = 8.dp
    )
}