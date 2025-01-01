@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.reader.misc

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategory
import ua.acclorite.book_story.presentation.settings.reader.misc.components.FullscreenOption
import ua.acclorite.book_story.presentation.settings.reader.misc.components.HideBarsOnFastScrollOption
import ua.acclorite.book_story.presentation.settings.reader.misc.components.KeepScreenOnOption

fun LazyListScope.MiscSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.misc_reader_settings) },
    showTitle: Boolean = true,
    showDivider: Boolean = true
) {
    SettingsSubcategory(
        titleColor = titleColor,
        title = title,
        showTitle = showTitle,
        showDivider = showDivider,
    ) {
        item {
            FullscreenOption()
        }

        item {
            KeepScreenOnOption()
        }

        item {
            HideBarsOnFastScrollOption()
        }
    }
}