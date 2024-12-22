@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.appearance

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.settings.appearance.colors.ColorsSubcategory
import ua.acclorite.book_story.presentation.settings.appearance.theme_preferences.ThemePreferencesSubcategory

fun LazyListScope.AppearanceSettingsCategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 16.dp
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