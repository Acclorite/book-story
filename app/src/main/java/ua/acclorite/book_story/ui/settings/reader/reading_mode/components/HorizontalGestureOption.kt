/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.reading_mode.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderHorizontalGesture
import ua.acclorite.book_story.ui.common.components.settings.ChipsWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ButtonItem

@Composable
fun HorizontalGestureOption() {
    val settings = LocalSettings.current

    ChipsWithTitle(
        title = stringResource(id = R.string.horizontal_gesture_option),
        chips = ReaderHorizontalGesture.entries.map {
            ButtonItem(
                id = it.name,
                title = when (it) {
                    ReaderHorizontalGesture.OFF -> stringResource(R.string.horizontal_gesture_off)
                    ReaderHorizontalGesture.ON -> stringResource(R.string.horizontal_gesture_on)
                    ReaderHorizontalGesture.INVERSE -> stringResource(R.string.horizontal_gesture_inverse)
                },
                textStyle = MaterialTheme.typography.labelLarge,
                selected = it == settings.horizontalGesture.value
            )
        },
        onClick = {
            settings.horizontalGesture.update(ReaderHorizontalGesture.valueOf(it.id))
        }
    )
}