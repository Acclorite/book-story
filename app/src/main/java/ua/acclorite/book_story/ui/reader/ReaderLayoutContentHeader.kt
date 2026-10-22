/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import ua.acclorite.book_story.domain.model.book.NodeContent
import ua.acclorite.book_story.ui.common.components.common.StyledText
import ua.acclorite.book_story.ui.reader.model.ReaderNode

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReaderLayoutContentHeader(
    header: ReaderNode.Header,
    header1TextStyle: TextStyle,
    header2TextStyle: TextStyle,
    header3TextStyle: TextStyle,
    header4TextStyle: TextStyle,
    header5TextStyle: TextStyle,
    header6TextStyle: TextStyle,
    sidePadding: Dp
) {
    Column(
        Modifier
            .fillMaxWidth()
    ) {
        StyledText(
            text = header.title,
            modifier = Modifier
                .padding(horizontal = sidePadding)
                .fillMaxWidth(),
            style = when (header.level) {
                NodeContent.HeaderLevel.H1 -> header1TextStyle
                NodeContent.HeaderLevel.H2 -> header2TextStyle
                NodeContent.HeaderLevel.H3 -> header3TextStyle
                NodeContent.HeaderLevel.H4 -> header4TextStyle
                NodeContent.HeaderLevel.H5 -> header5TextStyle
                NodeContent.HeaderLevel.H6 -> header6TextStyle
            }
        )
    }
}