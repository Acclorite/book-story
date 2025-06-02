/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.file.FileMapper
import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.data.model.library.BookWithCover
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.domain.file.File
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import ua.acclorite.book_story.domain.service.FileProvider
import ua.acclorite.book_story.ui.common.constants.provideExtensions
import javax.inject.Inject
import javax.inject.Singleton

/**
 * File System repository.
 * Manages all File System related work.
 */
@Singleton
class FileSystemRepositoryImpl @Inject constructor(
    private val database: BookDao,
    private val fileMapper: FileMapper,
    private val fileParser: FileParser,
    private val fileProvider: FileProvider
) : FileSystemRepository {

    override suspend fun searchFiles(query: String): Result<List<File>> {
        return fileProvider.getStorageFiles().mapCatching { storages ->
            val existingFiles = database.searchBooks("").map { it.filePath }

            storages.map { storage ->
                storage.getFilesFromStorage(
                    query = query,
                    existingFiles = existingFiles
                )
            }.flatten()
        }
    }

    private fun CachedFile.isValid(
        query: String,
        existingFiles: List<String>
    ): Boolean {
        if (provideExtensions().none { name.endsWith(it, ignoreCase = true) }) return false
        if (query.isNotBlank() && !name.contains(query.trim(), ignoreCase = true)) return false
        if (existingFiles.any { it.equals(path, ignoreCase = true) }) return false
        return true
    }

    private fun CachedFile.getFilesFromStorage(
        query: String,
        existingFiles: List<String>
    ): List<File> {
        val files = mutableListOf<File>()
        walk { cachedFile ->
            if (cachedFile.isValid(query, existingFiles)) {
                files.add(fileMapper.toFile(cachedFile))
            }
        }
        return files
    }

    override suspend fun getBookFromFile(file: File): Result<BookWithCover> = runCatching {
        fileParser.parse(fileMapper.toCachedFile(file))
            ?: throw Exception("Could not parse ${file.name}.")
    }
}