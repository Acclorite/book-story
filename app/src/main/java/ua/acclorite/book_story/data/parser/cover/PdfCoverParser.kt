/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.cover

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.data.storage.file.CachedFile
import javax.inject.Inject
import kotlin.math.roundToInt

private const val TAG = "PdfCoverParser"
private const val MAX_COVER_DIMENSION = 1024

class PdfCoverParser @Inject constructor() : CoverParser {

    override suspend fun parse(cachedFile: CachedFile): CoverImage? = withContext(Dispatchers.IO) {
        try {
            currentCoroutineContext().ensureActive()
            val rawFile = cachedFile.rawFile
            if (rawFile == null || !rawFile.exists() || !rawFile.canRead()) return@withContext null
            ParcelFileDescriptor.open(rawFile, ParcelFileDescriptor.MODE_READ_ONLY).use { descriptor ->
                PdfRenderer(descriptor).use { renderer ->
                    if (renderer.pageCount == 0) return@withContext null
                    renderer.openPage(0).use { page ->
                        currentCoroutineContext().ensureActive()
                        if (page.width <= 0 || page.height <= 0) return@withContext null
                        val scale = MAX_COVER_DIMENSION.toFloat() / maxOf(page.width, page.height)
                        val width = (page.width * scale).roundToInt().coerceIn(1, MAX_COVER_DIMENSION)
                        val height = (page.height * scale).roundToInt().coerceIn(1, MAX_COVER_DIMENSION)
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        try {
                            bitmap.eraseColor(Color.WHITE)
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            currentCoroutineContext().ensureActive()
                            bitmap
                        } catch (failure: Throwable) {
                            bitmap.recycle()
                            throw failure
                        }
                    }
                }
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (exception: Exception) {
            logE(TAG, "Could not render PDF cover with message: " + exception.message + ".")
            null
        }
    }
}