@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.reader

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.settings.reader.font.FontSubcategory
import ua.acclorite.book_story.presentation.settings.reader.misc.MiscSubcategory
import ua.acclorite.book_story.presentation.settings.reader.padding.PaddingSubcategory
import ua.acclorite.book_story.presentation.settings.reader.reading_mode.ReadingModeSubcategory
import ua.acclorite.book_story.presentation.settings.reader.reading_speed.ReadingSpeedSubcategory
import ua.acclorite.book_story.presentation.settings.reader.system.SystemSubcategory
import ua.acclorite.book_story.presentation.settings.reader.text.TextSubcategory
import ua.acclorite.book_story.presentation.settings.reader.translator.TranslatorSubcategory

fun LazyListScope.ReaderSettingsCategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 16.dp
) {
    FontSubcategory(
        titleColor = titleColor,
        topPadding = topPadding,
        bottomPadding = 0.dp
    )
    TextSubcategory(
        titleColor = titleColor,
        topPadding = 22.dp,
        bottomPadding = 0.dp
    )
    ReadingModeSubcategory(
        titleColor = titleColor,
        topPadding = 22.dp,
        bottomPadding = 0.dp
    )
    PaddingSubcategory(
        titleColor = titleColor,
        topPadding = 22.dp,
        bottomPadding = 0.dp
    )
    SystemSubcategory(
        titleColor = titleColor,
        topPadding = 22.dp,
        bottomPadding = 0.dp
    )
    ReadingSpeedSubcategory(
        titleColor = titleColor,
        topPadding = 22.dp,
        bottomPadding = 0.dp
    )
    TranslatorSubcategory(
        titleColor = titleColor,
        topPadding = 22.dp,
        bottomPadding = 0.dp
    )
    MiscSubcategory(
        titleColor = titleColor,
        showDivider = false,
        topPadding = 22.dp,
        bottomPadding = bottomPadding
    )
}