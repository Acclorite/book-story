@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.reader.reading_speed

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategory
import ua.acclorite.book_story.presentation.settings.reader.reading_speed.components.HighlightedReadingOption
import ua.acclorite.book_story.presentation.settings.reader.reading_speed.components.HighlightedReadingThicknessOption
import ua.acclorite.book_story.presentation.settings.reader.reading_speed.components.PerceptionExpanderOption
import ua.acclorite.book_story.presentation.settings.reader.reading_speed.components.PerceptionExpanderPaddingOption
import ua.acclorite.book_story.presentation.settings.reader.reading_speed.components.PerceptionExpanderThicknessOption

fun LazyListScope.ReadingSpeedSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.reading_speed_reader_settings) },
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
            HighlightedReadingOption()
        }

        item {
            HighlightedReadingThicknessOption()
        }

        item {
            PerceptionExpanderOption()
        }

        item {
            PerceptionExpanderPaddingOption()
        }

        item {
            PerceptionExpanderThicknessOption()
        }
    }
}