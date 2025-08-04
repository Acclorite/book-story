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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.language.Language
import ua.acclorite.book_story.ui.common.components.dialog.SelectableDialogItem
import ua.acclorite.book_story.ui.common.model.ListItem
import ua.acclorite.book_story.ui.settings.components.SettingsSubcategoryTitle

fun LazyListScope.StartSettingsLayoutGeneral(
    languages: List<ListItem<Language>>,
    updateLanguage: (Language) -> Unit,
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        SettingsSubcategoryTitle(
            title = stringResource(id = R.string.start_language_preferences),
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    items(languages) { item ->
        SelectableDialogItem(
            selected = item.selected,
            title = item.title,
            horizontalPadding = 18.dp
        ) {
            updateLanguage(item.item)
        }
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
    }
}