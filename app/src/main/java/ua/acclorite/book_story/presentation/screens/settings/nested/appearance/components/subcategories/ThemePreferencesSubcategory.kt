@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.subcategories

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.screens.settings.components.SettingsSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings.AbsoluteDarkSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings.DarkThemeSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings.PureDarkSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings.ThemeContrastSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings.ThemeSetting

/**
 * Theme Preferences subcategory.
 * Contains all settings from Theme Preferences.
 */
fun LazyListScope.ThemePreferencesSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.theme_appearance_settings) },
    showTitle: Boolean = true,
    showDivider: Boolean = true,
    topPadding: Dp,
    bottomPadding: Dp
) {
    SettingsSubcategory(
        titleColor = titleColor,
        title = title,
        showTitle = showTitle,
        showDivider = showDivider,
        topPadding = topPadding,
        bottomPadding = bottomPadding
    ) {
        item {
            DarkThemeSetting()
        }

        item {
            ThemeSetting()
        }

        item {
            ThemeContrastSetting()
        }

        item {
            PureDarkSetting()
        }

        item {
            AbsoluteDarkSetting()
        }
    }
}