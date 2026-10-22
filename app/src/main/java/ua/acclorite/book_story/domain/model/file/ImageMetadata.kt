/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.model.file

import androidx.compose.runtime.Immutable

@Immutable
data class ImageMetadata(
    val originalName: String,
    val path: FilePath,
    val width: Int,
    val height: Int,
    val aspectRatio: Float
)
