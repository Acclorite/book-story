/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.file_system

import ua.acclorite.book_story.domain.library.book.NullableBook
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import java.io.File
import javax.inject.Inject

class GetBookFromFile @Inject constructor(
    private val repository: FileSystemRepository
) {

    suspend fun execute(file: File): NullableBook {
        return repository.getBookFromFile(file)
    }
}