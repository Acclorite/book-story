package com.acclorite.books_history.presentation.screens.book_info.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun BookInfoBackground(image: ImageBitmap) {
    Image(
        bitmap = image,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .blur(1.5.dp),
        alpha = 0.3f,
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.51f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.surface
                    ),
                    startY = 0f,
                    endY = 750f
                )
            )
    )
}