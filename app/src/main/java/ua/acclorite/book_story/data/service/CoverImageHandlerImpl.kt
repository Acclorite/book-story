/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.service

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.domain.service.CoverImageHandler
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class CoverImageHandlerImpl @Inject constructor(
    application: Application
) : CoverImageHandler {

    private val filesDir: File = application.filesDir
    private val coversDir = File(filesDir, "covers")

    override suspend fun saveCover(coverImage: CoverImage): Result<File> = runCatching {
        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }

        val coverUri = "${UUID.randomUUID()}.webp"
        val cover = File(coversDir, coverUri)

        withContext(Dispatchers.IO) {
            BufferedOutputStream(FileOutputStream(cover)).use { output ->
                coverImage
                    .copy(Bitmap.Config.RGB_565, false)
                    .compress(Bitmap.CompressFormat.WEBP, 20, output)
                    .let { success ->
                        if (success) return@let
                        throw Exception("Couldn't save cover image.")
                    }
            }
        }

        cover
    }

    override suspend fun deleteCover(coverImage: Uri): Result<Unit> = runCatching {
        coverImage.toFile().apply {
            if (exists() && !delete()) throw Exception("Couldn't delete cover image.")
        }
    }

    override suspend fun compressCover(coverImage: CoverImage): Result<CoverImage> = runCatching {
        val stream = ByteArrayOutputStream()
        coverImage.copy(Bitmap.Config.RGB_565, false)
            .compress(Bitmap.CompressFormat.WEBP, 20, stream)
        val byteArray = stream.toByteArray()
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}