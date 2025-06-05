/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.file_system

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.file.File
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import javax.inject.Inject

class SearchFilesUseCase @Inject constructor(
    private val fileSystemRepository: FileSystemRepository
) {

    suspend operator fun invoke(query: String = ""): List<File> {
        logI("Searching for files with query: \"$query\".")

        return fileSystemRepository.searchFiles(query).fold(
            onSuccess = {
                logI("Successfully found [${it.size}] files.")
                it
            },
            onFailure = {
                logE("Could not find files with error: ${it.message}")
                emptyList()
            }
        )
    }
}