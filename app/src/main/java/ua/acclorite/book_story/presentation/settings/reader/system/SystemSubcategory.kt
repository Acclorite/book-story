@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.reader.system

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategory
import ua.acclorite.book_story.presentation.settings.reader.system.components.CustomScreenBrightnessOption
import ua.acclorite.book_story.presentation.settings.reader.system.components.ScreenBrightnessOption
import ua.acclorite.book_story.presentation.settings.reader.system.components.ScreenOrientationOption

fun LazyListScope.SystemSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.system_reader_settings) },
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
            CustomScreenBrightnessOption()
        }

        item {
            ScreenBrightnessOption()
        }

        item {
            ScreenOrientationOption()
        }
    }
}