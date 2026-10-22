/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.service

interface CompressionHandler {
    fun compressBlob(blob: ByteArray): ByteArray
    fun decompressBlob(blob: ByteArray): ByteArray
}