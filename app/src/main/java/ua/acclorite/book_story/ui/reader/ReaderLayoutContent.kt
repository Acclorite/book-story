/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import ua.acclorite.book_story.presentation.reader.ReaderEvent
import ua.acclorite.book_story.ui.reader.model.ReaderNode
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment

@Composable
fun ReaderLayoutContent(
    readerNode: ReaderNode,
    paragraphTextStyle: TextStyle,
    header1TextStyle: TextStyle,
    header2TextStyle: TextStyle,
    header3TextStyle: TextStyle,
    header4TextStyle: TextStyle,
    header5TextStyle: TextStyle,
    header6TextStyle: TextStyle,
    showMenu: Boolean,
    imagesCaptions: Boolean,
    imagesCornersRoundness: Dp,
    imagesAlignment: HorizontalAlignment,
    imagesWidth: Float,
    imagesColorEffects: ColorFilter?,
    imagesOriginalSize: Boolean,
    fontColor: Color,
    horizontalAlignment: Alignment.Horizontal,
    sidePadding: Dp,
    doubleClickTranslation: Boolean,
    toolbarHidden: Boolean,
    paragraphHeight: Dp,
    openTranslator: (ReaderEvent.OnOpenTranslator) -> Unit,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit
) {
    when (readerNode) {
        is ReaderNode.Header -> {
            ReaderLayoutContentHeader(
                header = readerNode,
                header1TextStyle = header1TextStyle,
                header2TextStyle = header2TextStyle,
                header3TextStyle = header3TextStyle,
                header4TextStyle = header4TextStyle,
                header5TextStyle = header5TextStyle,
                header6TextStyle = header6TextStyle,
                sidePadding = sidePadding
            )
        }

        is ReaderNode.Paragraph -> {
            ReaderLayoutContentParagraph(
                paragraph = readerNode,
                paragraphTextStyle = paragraphTextStyle,
                showMenu = showMenu,
                horizontalAlignment = horizontalAlignment,
                sidePadding = sidePadding,
                doubleClickTranslation = doubleClickTranslation,
                toolbarHidden = toolbarHidden,
                openTranslator = openTranslator,
                menuVisibility = menuVisibility
            )
        }

        is ReaderNode.Image -> {
            Column(verticalArrangement = Arrangement.spacedBy(paragraphHeight)) {
                ReaderLayoutContentImage(
                    image = readerNode,
                    fontColor = fontColor,
                    sidePadding = sidePadding,
                    imagesOriginalSize = imagesOriginalSize,
                    imagesCornersRoundness = imagesCornersRoundness,
                    imagesAlignment = imagesAlignment,
                    imagesWidth = imagesWidth,
                    imagesColorEffects = imagesColorEffects
                )

                if (imagesCaptions) {
                    ReaderLayoutContentParagraph(
                        paragraph = ReaderNode.Paragraph(
                            originalNode = readerNode.node,
                            text = buildAnnotatedString {
                                readerNode.caption?.let {
                                    withStyle(
                                        SpanStyle(
                                            fontStyle = FontStyle.Italic
                                        )
                                    ) { append(it) }
                                }
                            }
                        ),
                        paragraphTextStyle = paragraphTextStyle,
                        showMenu = showMenu,
                        horizontalAlignment = horizontalAlignment,
                        sidePadding = sidePadding,
                        doubleClickTranslation = doubleClickTranslation,
                        toolbarHidden = toolbarHidden,
                        openTranslator = openTranslator,
                        menuVisibility = menuVisibility
                    )
                }
            }
        }

        is ReaderNode.Separator -> {
            ReaderLayoutContentSeparator(
                sidePadding = sidePadding,
                fontColor = fontColor
            )
        }
    }
}