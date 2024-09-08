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
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings.ColorPresetSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings.FastColorPresetChangeSetting

/**
 * Colors subcategory.
 * Contains all settings from Colors.
 */
fun LazyListScope.ColorsSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.colors_appearance_settings) },
    backgroundColor: @Composable () -> Color = { MaterialTheme.colorScheme.surfaceContainerLow },
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
            ColorPresetSetting(backgroundColor = backgroundColor.invoke())
        }

        item {
            FastColorPresetChangeSetting()
        }
    }
}