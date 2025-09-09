/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.repository

import android.app.Application
import android.util.Log
import ua.blindmint.codex.R
import ua.blindmint.codex.data.local.room.BookDao
import ua.blindmint.codex.data.parser.FileParser
import ua.blindmint.codex.domain.browse.file.SelectableFile
import ua.blindmint.codex.domain.file.CachedFile
import ua.blindmint.codex.domain.file.CachedFileCompat
import ua.blindmint.codex.domain.library.book.NullableBook
import ua.blindmint.codex.domain.library.book.NullableBook.NotNull
import ua.blindmint.codex.domain.library.book.NullableBook.Null
import ua.blindmint.codex.domain.repository.FileSystemRepository
import ua.blindmint.codex.domain.ui.UIText
import ua.blindmint.codex.presentation.core.constants.provideExtensions
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val GET_BOOK_FROM_FILE = "BOOK FROM FILE, REPO"
private const val GET_FILES = "FILES, REPO"

/**
 * File System repository.
 * Manages all File System related work.
 */
@Singleton
class FileSystemRepositoryImpl @Inject constructor(
    private val application: Application,
    private val database: BookDao,
    private val fileParser: FileParser
) : FileSystemRepository {

    /**
     * Gets all matching files from device.
     * Filters by [query] and sorts out not supported file formats and already added files.
     */
    override suspend fun getFiles(query: String): List<SelectableFile> {
        Log.i(GET_FILES, "Getting files from device, query: \"$query\".")

        val existingPaths = database
            .searchBooks("")
            .map { it.filePath }
        val supportedExtensions = provideExtensions()

        /**
         * Verify that [CachedFile] is valid and can be shown correctly.
         */
        fun CachedFile.isValid(): Boolean {
            // First: Ensuring supported extension
            supportedExtensions.any { ext ->
                name.endsWith(ext, ignoreCase = true)
            }.let { if (!it) return false }

            // Second: Ensuring query to match
            if (query.isNotBlank()) {
                name.contains(query.trim(), ignoreCase = true).let {
                    if (!it) return false
                }
            }

            // Third: Ensuring that a file is not added already
            existingPaths.none { existingPath ->
                existingPath.equals(path, ignoreCase = true)
            }.let { if (!it) return false }

            return true
        }

        fun CachedFile.getSelectableFilesFromStorage(): List<SelectableFile> {
            val selectableFiles = mutableListOf<SelectableFile>()

            walk { file ->
                if (!file.isValid()) return@walk

                selectableFiles.add(
                    SelectableFile(
                        data = file,
                        selected = false
                    )
                )
            }

            return selectableFiles
        }

        fun getAllStorages(): List<CachedFile> {
            return application.contentResolver.persistedUriPermissions.mapNotNull { permission ->
                val storage = CachedFileCompat.fromUri(
                    application,
                    permission.uri,
                    builder = CachedFileCompat.build(
                        name = UUID.randomUUID().toString(),
                        size = 0,
                        lastModified = 0
                    )
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

        return try {
            val storages = getAllStorages()
            val files = mutableListOf<SelectableFile>()

            for (storage in storages) {
                files.addAll(storage.getSelectableFilesFromStorage())
            }

            Log.i(GET_FILES, "Successfully got all matching files.")
            files
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(GET_FILES, "Couldn't get all matching files.")

            emptyList()
        }
    }

    /**
     * Gets book from given file. If error happened, returns [NullableBook.Null].
     */
    override suspend fun getBookFromFile(cachedFile: CachedFile): NullableBook {
        val parsedBook = fileParser.parse(cachedFile)
        if (parsedBook == null) {
            Log.e(GET_BOOK_FROM_FILE, "Parsed file(${cachedFile.name}) is null.")
            return Null(
                cachedFile.name,
                UIText.StringResource(R.string.error_something_went_wrong)
            )
        }

        Log.i(GET_BOOK_FROM_FILE, "Successfully got book from file.")
        return NotNull(bookWithCover = parsedBook)
    }
}