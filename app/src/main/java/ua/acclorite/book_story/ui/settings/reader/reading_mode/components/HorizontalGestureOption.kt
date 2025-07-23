/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.reading_mode.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.ui.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem

@Composable
fun HorizontalGestureOption() {
    val settings = LocalSettings.current

    ChipsWithTitle(
        title = stringResource(id = R.string.horizontal_gesture_option),
        chips = ReaderHorizontalGesture.entries.map { item ->
            ListItem(
                item = item,
                title = stringResource(id = item.title),
                selected = item == settings.horizontalGesture.value
            )
        },
        onClick = { item ->
            settings.horizontalGesture.update(item)
        }
    )
}