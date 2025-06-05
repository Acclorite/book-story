/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.ui.start

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.settings.browse.scan.components.BrowseScanOption
import ua.acclorite.book_story.ui.settings.components.SettingsSubcategoryTitle

fun LazyListScope.StartSettingsLayoutScan() {
    item {
        Spacer(modifier = Modifier.height(16.dp))
    }

    item {
        SettingsSubcategoryTitle(
            title = stringResource(id = R.string.start_scan_preferences),
            color = MaterialTheme.colorScheme.secondary
        )
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
    }

    item {
        BrowseScanOption()
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
    }
}