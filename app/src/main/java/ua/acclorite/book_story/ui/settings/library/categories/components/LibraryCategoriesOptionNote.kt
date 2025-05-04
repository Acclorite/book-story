/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.library.categories.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.settings.components.SettingsSubcategoryNote

@Composable
fun LibraryCategoriesOptionNote() {
    SettingsSubcategoryNote(
        text = stringResource(id = R.string.library_categories_option_note)
    )
}