/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ua.acclorite.book_story.ui.reader.model.ReaderNode
import ua.acclorite.book_story.ui.theme.model.HorizontalAlignment

@Composable
fun ReaderLayoutContentImage(
    image: ReaderNode.Image,
    fontColor: Color,
    sidePadding: Dp,
    imagesOriginalSize: Boolean,
    imagesCornersRoundness: Dp,
    imagesAlignment: HorizontalAlignment,
    imagesWidth: Float,
    imagesColorEffects: ColorFilter?
) {
    Box(
        modifier = Modifier
            .padding(horizontal = sidePadding)
            .fillMaxWidth(),
        contentAlignment = imagesAlignment.alignment
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.content)
                .crossfade(300)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(imagesCornersRoundness))
                .background(fontColor.copy(0.3f))
                .then(
                    if (imagesOriginalSize) {
                        Modifier
                            .width(image.originalWidth)
                            .height(image.originalHeight)
                    } else {
                        Modifier.fillMaxWidth(imagesWidth)
                    }
                ),
            contentScale = ContentScale.FillWidth,
            colorFilter = imagesColorEffects
        )
    }
}