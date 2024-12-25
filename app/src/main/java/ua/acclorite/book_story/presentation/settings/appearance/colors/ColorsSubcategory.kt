@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.appearance.colors

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.appearance.colors.components.ColorPresetOption
import ua.acclorite.book_story.presentation.settings.appearance.colors.components.FastColorPresetChangeOption
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategory

fun LazyListScope.ColorsSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.colors_appearance_settings) },
    backgroundColor: @Composable () -> Color = { MaterialTheme.colorScheme.surfaceContainerLow },
    showTitle: Boolean = true,
    showDivider: Boolean = true
) {
    SettingsSubcategory(
        titleColor = titleColor,
        title = title,
        showTitle = showTitle,
        showDivider = showDivider
    ) {
        item {
            ColorPresetOption(
                backgroundColor = backgroundColor.invoke()
            )
        }

        item {
            FastColorPresetChangeOption()
        }
    }
}