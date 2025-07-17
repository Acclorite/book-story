/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.library.sort.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.settings.SwitchWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings

@Composable
fun LibraryPerCategorySortOption() {
    val settings = LocalSettings.current

    SwitchWithTitle(
        selected = settings.libraryPerCategorySort.value,
        title = stringResource(id = R.string.per_category_sort_option),
        onClick = {
            settings.libraryPerCategorySort.update(!settings.libraryPerCategorySort.lastValue)
        }
    )
}