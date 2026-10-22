/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.service

import android.net.Uri
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import java.io.File
import java.io.InputStream

interface ImageHandler {
    suspend fun saveCover(coverImage: CoverImage): Result<File>
    suspend fun deleteCover(coverImage: Uri): Result<Unit>
    suspend fun compressCover(coverImage: CoverImage): Result<CoverImage>
    suspend fun extractImageBoundsFromFile(file: FilePath): Result<Triple<Int, Int, Float>>
    suspend fun cacheImageAndGetMetadata(
        filename: String,
        inputStream: InputStream,
        tempDirectory: String = ""
    ): Result<ImageMetadata?>
}