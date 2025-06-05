/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.ui.settings.reader

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.ui.settings.reader.chapters.ChaptersSubcategory
import ua.acclorite.book_story.ui.settings.reader.font.FontSubcategory
import ua.acclorite.book_story.ui.settings.reader.images.ImagesSubcategory
import ua.acclorite.book_story.ui.settings.reader.misc.MiscSubcategory
import ua.acclorite.book_story.ui.settings.reader.padding.PaddingSubcategory
import ua.acclorite.book_story.ui.settings.reader.progress.ProgressSubcategory
import ua.acclorite.book_story.ui.settings.reader.reading_mode.ReadingModeSubcategory
import ua.acclorite.book_story.ui.settings.reader.reading_speed.ReadingSpeedSubcategory
import ua.acclorite.book_story.ui.settings.reader.system.SystemSubcategory
import ua.acclorite.book_story.ui.settings.reader.text.TextSubcategory
import ua.acclorite.book_story.ui.settings.reader.translator.TranslatorSubcategory

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
    ProgressSubcategory(
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