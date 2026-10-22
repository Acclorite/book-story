/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.service

import ua.acclorite.book_story.domain.service.CompressionHandler
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.inject.Inject

class GZIPCompressionHandlerImpl @Inject constructor() : CompressionHandler {

    override fun compressBlob(blob: ByteArray): ByteArray {
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            GZIPOutputStream(byteArrayOutputStream).use { gZIPOutputStream ->
                gZIPOutputStream.write(blob)
            }
            return byteArrayOutputStream.toByteArray()
        }
    }

    override fun decompressBlob(blob: ByteArray): ByteArray {
        ByteArrayInputStream(blob).use { byteArrayInputStream ->
            GZIPInputStream(byteArrayInputStream).use { gZIPInputStream ->
                return gZIPInputStream.readBytes()
            }
        }
    }
}