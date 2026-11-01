/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.cover

import android.graphics.BitmapFactory
import android.util.Xml
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.data.storage.file.CachedFile
import java.net.URI
import javax.inject.Inject
import kotlin.io.encoding.Base64

private const val TAG = "Fb2CoverParser"
private const val XLINK_NAMESPACE = "http://www.w3.org/1999/xlink"
private const val MAX_COVER_DIMENSION = 1024
private val coverImagePath = listOf("FictionBook", "description", "title-info", "coverpage", "image")

class Fb2CoverParser @Inject constructor() : CoverParser {

    override suspend fun parse(cachedFile: CachedFile): CoverImage? = withContext(Dispatchers.IO) {
        try {
            cachedFile.openInputStream()?.use { stream ->
                val parser = Xml.newPullParser().apply {
                    setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
                    setInput(stream, null)
                }
                val path = ArrayDeque<String>()
                var coverId: String? = null

                while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                    currentCoroutineContext().ensureActive()
                    when (parser.eventType) {
                        XmlPullParser.START_TAG -> {
                            path.addLast(parser.name)
                            if (path.size == 1 && parser.name != "FictionBook") return@use null
                            if (coverId == null && parser.name == "image" && path.toList() == coverImagePath) {
                                val reference = (
                                    parser.getAttributeValue(XLINK_NAMESPACE, "href")
                                        ?: parser.getAttributeValue(null, "href")
                                    )?.trim() ?: return@use null
                                if (!reference.startsWith('#')) return@use null
                                coverId = URI(reference).fragment?.takeIf { it.isNotBlank() }
                                    ?: return@use null
                            }
                            if (path.size == 2) {
                                if (parser.name == "body" && coverId == null) return@use null
                                if (parser.name == "binary" && coverId != null &&
                                    parser.getAttributeValue(null, "id") == coverId
                                ) {
                                    val mime = parser.getAttributeValue(null, "content-type")?.trim()
                                    if (mime?.startsWith("image/", ignoreCase = true) != true) return@use null
                                    val encoded = parser.nextText()
                                    currentCoroutineContext().ensureActive()
                                    return@use decodeCover(encoded)
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> path.removeLast()
                    }
                    parser.next()
                }
                null
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (exception: Exception) {
            logE(TAG, "Could not parse cover image with message: " + exception.message + ".")
            null
        }
    }

    private fun decodeCover(encoded: String): CoverImage? {
        val bytes = Base64.decode(encoded.filterNot { it in " \t\r\n" })
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null
        var sampleSize = 1
        val longestSide = maxOf(bounds.outWidth, bounds.outHeight)
        while (longestSide.toLong() > MAX_COVER_DIMENSION.toLong() * sampleSize) sampleSize *= 2
        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }
}