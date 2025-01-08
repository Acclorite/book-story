@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.reader

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.presentation.settings.reader.chapters.ChaptersSubcategory
import ua.acclorite.book_story.presentation.settings.reader.font.FontSubcategory
import ua.acclorite.book_story.presentation.settings.reader.images.ImagesSubcategory
import ua.acclorite.book_story.presentation.settings.reader.misc.MiscSubcategory
import ua.acclorite.book_story.presentation.settings.reader.padding.PaddingSubcategory
import ua.acclorite.book_story.presentation.settings.reader.reading_mode.ReadingModeSubcategory
import ua.acclorite.book_story.presentation.settings.reader.reading_speed.ReadingSpeedSubcategory
import ua.acclorite.book_story.presentation.settings.reader.system.SystemSubcategory
import ua.acclorite.book_story.presentation.settings.reader.text.TextSubcategory
import ua.acclorite.book_story.presentation.settings.reader.translator.TranslatorSubcategory

fun LazyListScope.ReaderSettingsCategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary }
) {
    FontSubcategory(
        titleColor = titleColor
    )
    TextSubcategory(
        titleColor = titleColor
    )
    ImagesSubcategory(
        titleColor = titleColor
    )
    ChaptersSubcategory(
        titleColor = titleColor
    )
    ReadingModeSubcategory(
        titleColor = titleColor
    )
    PaddingSubcategory(
        titleColor = titleColor
    )
    SystemSubcategory(
        titleColor = titleColor
    )
    ReadingSpeedSubcategory(
        titleColor = titleColor
    )
    TranslatorSubcategory(
        titleColor = titleColor
    )
    MiscSubcategory(
        titleColor = titleColor,
        showDivider = false
    )
}