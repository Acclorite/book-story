/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.ui.settings.library

import androidx.compose.foundation.lazy.LazyListScope
import ua.acclorite.book_story.ui.settings.library.categories.LibraryCategoriesSubcategory
import ua.acclorite.book_story.ui.settings.library.display.LibraryDisplaySubcategory
import ua.acclorite.book_story.ui.settings.library.sort.LibrarySortSubcategory
import ua.acclorite.book_story.ui.settings.library.tabs.LibraryTabsSubcategory

fun LazyListScope.LibrarySettingsCategory() {
    LibraryCategoriesSubcategory()
    LibraryDisplaySubcategory()
    LibraryTabsSubcategory()
    LibrarySortSubcategory(
        showDivider = false
    )
}