/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.service

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toFile
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.domain.model.file.FilePath
import ua.acclorite.book_story.domain.model.file.ImageMetadata
import ua.acclorite.book_story.domain.service.FileHandler
import ua.acclorite.book_story.domain.service.ImageHandler
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

private const val COVERS_DIRECTORY = "covers"

class ImageHandlerImpl @Inject constructor(
    application: Application,
    private val fileHandler: FileHandler
) : ImageHandler {

    private val filesDir: File = application.filesDir
    private val coversDir = File(filesDir, COVERS_DIRECTORY)

    /**
     * Saves cover image to [coversDir] with compression.
     */
    override suspend fun saveCover(coverImage: CoverImage): Result<File> = runCatching {
        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }

        val coverUri = "${UUID.randomUUID()}.webp"
        val cover = File(coversDir, coverUri)

        BufferedOutputStream(FileOutputStream(cover)).use { output ->
            coverImage
                .copy(Bitmap.Config.RGB_565, false)
                .compress(Bitmap.CompressFormat.WEBP, 20, output)
                .let { success ->
                    if (success) return@let
                    throw Exception("Couldn't save cover image")
                }
        }

        cover
    }

    override suspend fun deleteCover(coverImage: Uri): Result<Unit> = runCatching {
        coverImage.toFile().apply {
            if (exists() && !delete()) throw Exception("Couldn't delete cover image")
        }
    }

    override suspend fun compressCover(coverImage: CoverImage): Result<CoverImage> = runCatching {
        val stream = ByteArrayOutputStream()
        coverImage.copy(Bitmap.Config.RGB_565, false)
            .compress(Bitmap.CompressFormat.WEBP, 20, stream)
        val byteArray = stream.toByteArray()
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    /**
     * Extracts image bounds (width, height and aspect ratio) from [FilePath].
     * Does not load the whole image.
     */
    override suspend fun extractImageBoundsFromFile(file: FilePath): Result<Triple<Int, Int, Float>> =
        runCatching {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(file.path, options)

            val width = options.outWidth
            val height = options.outHeight
            if (width <= 0 || height <= 0) error("Could not extract image bounds from file: [${file.path}]")

            val aspectRatio = width.toFloat() / height.toFloat()
            Triple(width, height, aspectRatio)
        }

    override suspend fun cacheImageAndGetMetadata(
        filename: String,
        inputStream: InputStream,
        tempDirectory: String
    ): Result<ImageMetadata?> = runCatching {
        if (filename.isBlank()) return@runCatching null

        val path = fileHandler.saveStreamToCacheWithRandomName(
            inputStream = inputStream,
            extension = filename.substringAfterLast("."),
            directory = tempDirectory
        ).getOrElse { return@runCatching null }

        val (width, height, aspectRatio) = extractImageBoundsFromFile(
            file = path
        ).getOrElse { return@runCatching null }

        val originalName = filename.substringAfterLast(
            File.separator
        ).lowercase()
        ImageMetadata(
            originalName = originalName,
            path = path,
            width = width,
            height = height,
            aspectRatio = aspectRatio
        )
    }
}