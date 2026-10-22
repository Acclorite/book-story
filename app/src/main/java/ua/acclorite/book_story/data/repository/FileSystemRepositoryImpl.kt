/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.core.data.ExtensionsData
import ua.acclorite.book_story.data.mapper.file.FileMapper
import ua.acclorite.book_story.data.parser.cover.CoverParser
import ua.acclorite.book_story.data.parser.file.FileParser
import ua.acclorite.book_story.data.storage.file.CachedFile
import ua.acclorite.book_story.data.storage.room.AppDatabase
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import ua.acclorite.book_story.domain.service.FileHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileSystemRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val fileMapper: FileMapper,
    private val fileParser: FileParser,
    private val coverParser: CoverParser,
    private val fileHandler: FileHandler
) : FileSystemRepository {

    override suspend fun searchFiles(query: String): Result<List<File>> {
        return withContext(Dispatchers.IO) {
            fileHandler.getStorageFiles().mapCatching { storages ->
                val existingFiles = appDatabase.bookDao.searchBooks("").map { it.filePath }

                storages.flatMap { storage ->
                    storage.getFilesFromStorage(
                        query = query,
                        existingFiles = existingFiles
                    )
                }
            }
        }
    }

    private fun CachedFile.isValid(
        query: String,
        existingFiles: List<String>
    ): Boolean {
        if (
            ExtensionsData.fileExtensions.none {
                name.endsWith(
                    it,
                    ignoreCase = true
                )
            }
        ) return false
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

    override suspend fun getBookFromFile(file: File): Result<Pair<Book, CoverImage?>> =
        runCatching {
            withContext(Dispatchers.IO) {
                val cachedFile = fileMapper.toCachedFile(file)

                val book = fileParser.parse(
                    cachedFile = cachedFile
                ) ?: throw Exception("Could not parse ${file.name}")
                val coverImage = coverParser.parse(cachedFile = cachedFile)

                return@withContext book to coverImage
            }
        }
}