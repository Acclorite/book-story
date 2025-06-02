/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.service

import android.net.Uri
import ua.acclorite.book_story.core.CoverImage
import java.io.File

interface CoverImageHandler {
    suspend fun saveCover(coverImage: CoverImage): Result<File>
    suspend fun deleteCover(coverImage: Uri): Result<Unit>
    suspend fun compressCover(coverImage: CoverImage): Result<CoverImage>
}