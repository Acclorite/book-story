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
        application.contentResolver.persistedUriPermissions.forEach { storage ->
            val storageFile = CachedFileCompat.fromUri(
                application,
                storage.uri
            )

            if (!storageFile.isDirectory) return@forEach
            if (!book.filePath.startsWith(storageFile.path, ignoreCase = true)) return@forEach

            storageFile.walk().forEach { file ->
                if (book.filePath.equals(file.path, ignoreCase = true)) {
                    return@runCatching file
                }
            }
        }

        throw NoSuchElementException("Could not find file from book.")
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