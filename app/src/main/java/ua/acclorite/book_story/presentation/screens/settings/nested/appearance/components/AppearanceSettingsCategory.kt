@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.subcategories.ColorsSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.subcategories.ThemePreferencesSubcategory

/**
 * Appearance Settings Category.
 * Contains all Appearance settings such as Theme.
 *
 * @param titleColor Title's color.
 * @param topPadding Top padding to be applied.
 * @param bottomPadding Bottom padding to be applied.
 */
fun LazyListScope.AppearanceSettingsCategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 48.dp
) {
    ThemePreferencesSubcategory(
        titleColor = titleColor,
        topPadding = topPadding,
        bottomPadding = 0.dp
    )
    ColorsSubcategory(
        titleColor = titleColor,
        showDivider = false,
        topPadding = 22.dp,
        bottomPadding = bottomPadding
    )
}