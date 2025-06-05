/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.ui.settings.browse

import androidx.compose.foundation.lazy.LazyListScope
import ua.acclorite.book_story.ui.settings.browse.display.BrowseDisplaySubcategory
import ua.acclorite.book_story.ui.settings.browse.scan.BrowseScanSubcategory

fun LazyListScope.BrowseSettingsCategory() {
    BrowseScanSubcategory()
    BrowseDisplaySubcategory(
        showDivider = false
    )
}