@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.reader.text

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategory
import ua.acclorite.book_story.presentation.settings.reader.text.components.LineHeightOption
import ua.acclorite.book_story.presentation.settings.reader.text.components.ParagraphHeightOption
import ua.acclorite.book_story.presentation.settings.reader.text.components.ParagraphIndentationOption
import ua.acclorite.book_story.presentation.settings.reader.text.components.TextAlignmentOption

fun LazyListScope.TextSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.text_reader_settings) },
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
            TextAlignmentOption()
        }

        item {
            LineHeightOption()
        }

        item {
            ParagraphHeightOption()
        }

        item {
            ParagraphIndentationOption()
        }
    }
}