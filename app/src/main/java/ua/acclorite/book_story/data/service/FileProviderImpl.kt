/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.service

import android.app.Application
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.data.model.file.CachedFileCompat
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.domain.service.FileProvider
import javax.inject.Inject

class FileProviderImpl @Inject constructor(
    private val application: Application
) : FileProvider {

    override fun getFileFromBook(book: Book): Result<CachedFile> = runCatching {
        CachedFileCompat.fromFullPath(
            context = application,
            path = book.filePath,
            builder = CachedFileCompat.build(
                name = book.filePath.substringAfterLast(java.io.File.separator),
                path = book.filePath,
                isDirectory = false
            )
        ).let { file ->
            if (file == null || !file.canAccess()) {
                throw NoSuchElementException("Could not load CachedFile.")
            }

            file
        }
    }

    override fun getStorageFiles(): Result<List<CachedFile>> = runCatching {
        application.contentResolver.persistedUriPermissions.mapNotNull { permission ->
            val storage = CachedFileCompat.fromUri(
                application,
                permission.uri
            )
            if (!storage.isDirectory) return@mapNotNull null

            storage
        }.let { storages ->
            storages.filter { storage ->
                storages.none {
                    it.path != storage.path && storage.path.startsWith(
                        it.path,
                        ignoreCase = true
                    )
                }
            }
        }
    }
}