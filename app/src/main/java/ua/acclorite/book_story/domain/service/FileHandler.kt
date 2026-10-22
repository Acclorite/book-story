/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.service

import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.file.FilePath
import java.io.InputStream
import java.io.OutputStream

@JvmInline
value class FileHash(val hash: String)

interface FileHandler {
    fun getFileFromBook(book: Book): Result<CachedFile>
    fun getFileHashFromBook(book: Book): Result<FileHash>
    fun getParserFromBook(book: Book): Result<String>
    fun getStorageFiles(): Result<List<CachedFile>>

    fun saveCustomToCacheWithRandomName(
        save: (OutputStream) -> Unit,
        extension: String,
        directory: String
    ): Result<FilePath>

    fun saveStreamToCacheWithRandomName(
        inputStream: InputStream,
        extension: String,
        directory: String
    ): Result<FilePath>

    fun moveFile(
        fromPath: FilePath,
        toPath: FilePath
    ): Result<FilePath>

    fun deleteDirectory(
        directory: FilePath
    ): Result<Unit>
}