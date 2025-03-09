/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import ua.acclorite.book_story.domain.browse.file.SelectableFile
import ua.acclorite.book_story.domain.file.CachedFile
import ua.acclorite.book_story.domain.library.book.NullableBook

interface FileSystemRepository {

    suspend fun getFiles(
        query: String = ""
    ): List<SelectableFile>

    suspend fun getBookFromFile(
        cachedFile: CachedFile
    ): NullableBook
}