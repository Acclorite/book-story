/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.file_system

import ua.acclorite.book_story.domain.file.File
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import javax.inject.Inject

class GetFiles @Inject constructor(
    private val repository: FileSystemRepository
) {

    suspend fun execute(query: String): List<File> {
        return repository.getFiles(query)
    }
}