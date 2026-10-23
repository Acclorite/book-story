/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings.reader.headers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.reader.model.ReaderTextAlignment
import ua.acclorite.book_story.ui.common.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.common.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.common.helpers.LocalSettings
import ua.acclorite.book_story.ui.common.model.ListItem

private var initialSelectedHeader = 0

@Composable
fun HeaderOptions(backgroundColor: Color) {
    val settings = LocalSettings.current
    val selectedHeader = rememberSaveable { mutableIntStateOf(initialSelectedHeader) }
    DisposableEffect(Unit) {
        onDispose { initialSelectedHeader = selectedHeader.intValue }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SegmentedButtonWithTitle(
            buttons = settings.headers.indices.map { index ->
                ListItem(
                    item = index,
                    title = stringResource(id = R.string.header_level_label, index + 1),
                    selected = index == selectedHeader.intValue
                )
            },
            onClick = { selectedHeader.intValue = it }
        )

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(MaterialTheme.shapes.large)
                .background(backgroundColor)
                .padding(vertical = 10.dp)
        ) {
            key(selectedHeader.intValue) {
                val header = settings.headers[selectedHeader.intValue]

                SliderWithTitle(
                    value = header.fontSize.value to "pt",
                    fromValue = 10,
                    toValue = 72,
                    title = stringResource(id = R.string.font_size_option),
                    onValueChange = { header.fontSize.update(it) }
                )

                SliderWithTitle(
                    value = header.fontWeight.value / 100 to "",
                    valuePlaceholder = header.fontWeight.value.toString(),
                    showPlaceholder = true,
                    fromValue = 1,
                    toValue = 9,
                    title = stringResource(id = R.string.font_thickness_option),
                    onValueChange = { header.fontWeight.update(it * 100) }
                )

                SegmentedButtonWithTitle(
                    title = stringResource(id = R.string.text_alignment_option),
                    buttons = ReaderTextAlignment.entries.map { alignment ->
                        ListItem(
                            item = alignment,
                            title = stringResource(id = alignment.title),
                            selected = alignment == header.textAlignment.value
                        )
                    },
                    onClick = { header.textAlignment.update(it) }
                )
            }
        }
    }
}
