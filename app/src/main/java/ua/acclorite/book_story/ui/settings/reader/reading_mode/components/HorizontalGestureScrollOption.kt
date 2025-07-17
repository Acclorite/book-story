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
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.theme.ExpandingTransition

@Composable
fun HorizontalGestureScrollOption() {
    val settings = LocalSettings.current

    ExpandingTransition(
        visible = when (settings.horizontalGesture.value) {
            ReaderHorizontalGesture.OFF -> false
            else -> true
        }
    ) {
        SliderWithTitle(
            value = settings.horizontalGestureScroll.value to "%",
            toValue = 100,
            title = stringResource(id = R.string.horizontal_gesture_scroll_option),
            onValueChange = {
                settings.horizontalGestureScroll.update(it)
            }
        )
    }
}