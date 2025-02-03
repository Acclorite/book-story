/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import android.app.Application
import android.content.Context.STORAGE_SERVICE
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.domain.library.book.NullableBook
import ua.acclorite.book_story.domain.library.book.NullableBook.NotNull
import ua.acclorite.book_story.domain.library.book.NullableBook.Null
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideExtensions
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val GET_BOOK_FROM_FILE = "BOOK FROM FILE, REPO"
private const val GET_FILES_FROM_DEVICE = "FILES FROM DEVICE, REPO"

/**
 * File System repository.
 * Manages all File System([java.io.File]) related work.
 */
@Singleton
class FileSystemRepositoryImpl @Inject constructor(
    private val application: Application,
    private val database: BookDao,
    private val fileParser: FileParser
) : FileSystemRepository {

    /**
     * Get all matching files from device.
     * Filters by [query] and sorts out not supported file formats and already added files.
     */
    override suspend fun getFilesFromDevice(query: String): List<SelectableFile> {
        Log.i(GET_FILES_FROM_DEVICE, "Getting files from device by query: \"$query\".")

        val existingPaths = database
            .searchBooks("")
            .map { it.filePath.trim() }
        val supportedExtensions = Constants.provideExtensions()

        /**
         * Verify that a file is valid and can be shown correctly.
         */
        fun File.isValid(): Boolean {
            if (!exists() || !canRead() || !isFile) return false

            // First: Ensuring supported extension
            supportedExtensions.any { ext ->
                name.endsWith(ext, ignoreCase = true)
            }.let { if (!it) return false }

            // Second: Ensuring query to match
            if (query.isNotBlank()) {
                name.contains(query.trim(), ignoreCase = true)
                    .let { if (!it) return false }
            }

            // Third: Ensuring that a file is not added already
            existingPaths.none { existingPath ->
                existingPath.equals(path.trim(), ignoreCase = true)
            }.let { if (!it) return false }

            return true
        }

        /**
         * Get all verified files from directory (root).
         */
        suspend fun File.getFilesFromDirectory(): List<SelectableFile> {
            return withContext(Dispatchers.IO) {
                val semaphore = Semaphore(5)
                walk()
                    .map { file ->
                        async {
                            semaphore.withPermit {
                                if (!file.isValid()) return@async null
                                SelectableFile(
                                    name = file.name,
                                    path = file.path,
                                    size = file.length(),
                                    lastModified = file.lastModified(),
                                    selected = false
                                )
                            }
                        }
                    }
                    .toList()
                    .awaitAll()
                    .filterNotNull()
            }
        }

        /**
         * Get all storages (including SD).
         */
        fun getAllStorageDirectories(): List<File> {
            val storageDirectories = mutableListOf<File>()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val storageManager = application.getSystemService(STORAGE_SERVICE) as StorageManager

                val storageVolumes = storageManager.storageVolumes.mapNotNull { it.directory }
                for (volume in storageVolumes) {
                    if (volume.exists() && volume.canRead() && volume.isDirectory) {
                        storageDirectories.add(volume)
                    }
                }
            } else {
                val internalStorage = Environment.getExternalStorageDirectory()
                if (internalStorage.exists() && internalStorage.canRead() && internalStorage.isDirectory) {
                    storageDirectories.add(internalStorage)
                }

                val storageVolumes = File("/storage")
                if (storageVolumes.exists() && storageVolumes.canRead() && storageVolumes.isDirectory) {
                    storageVolumes.listFiles()?.forEach { volume ->
                        if (
                            volume.exists() &&
                            volume.canRead() &&
                            volume.isDirectory &&
                            volume != internalStorage
                        ) {
                            storageDirectories.add(volume)
                        }
                    }
                }
            }

            return storageDirectories
        }

        val storageDirectories = getAllStorageDirectories()
        val files = mutableListOf<SelectableFile>()

        for (volume in storageDirectories) {
            files.addAll(volume.getFilesFromDirectory())
        }

        Log.i(GET_FILES_FROM_DEVICE, "Successfully got all matching files.")
        return files
    }

    /**
     * Gets book from given file. If error happened, returns [NullableBook.Null].
     */
    override suspend fun getBookFromFile(file: File): NullableBook {
        val parsedBook = fileParser.parse(file)
        if (parsedBook == null) {
            Log.e(GET_BOOK_FROM_FILE, "Parsed file(${file.name}) is null.")
            return Null(
                file.name,
                UIText.StringResource(R.string.error_something_went_wrong)
            )
        }

        Log.i(GET_BOOK_FROM_FILE, "Successfully got book from file.")
        return NotNull(bookWithCover = parsedBook)
    }
}