@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.reader.progress

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategory
import ua.acclorite.book_story.presentation.settings.reader.progress.components.ProgressBarAlignmentOption
import ua.acclorite.book_story.presentation.settings.reader.progress.components.ProgressBarFontSizeOption
import ua.acclorite.book_story.presentation.settings.reader.progress.components.ProgressBarOption
import ua.acclorite.book_story.presentation.settings.reader.progress.components.ProgressBarPaddingOption
import ua.acclorite.book_story.presentation.settings.reader.progress.components.ProgressCountOption

fun LazyListScope.ProgressSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.progress_reader_settings) },
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
            ProgressCountOption()
        }

        item {
            ProgressBarOption()
        }

        item {
            ProgressBarFontSizeOption()
        }

        item {
            ProgressBarPaddingOption()
        }

        item {
            ProgressBarAlignmentOption()
        }
    }
}