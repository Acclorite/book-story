/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.repository

import ua.blindmint.codex.domain.browse.file.SelectableFile
import ua.blindmint.codex.domain.file.CachedFile
import ua.blindmint.codex.domain.library.book.NullableBook

interface FileSystemRepository {

    suspend fun getFiles(
        query: String = ""
    ): List<SelectableFile>

    suspend fun getBookFromFile(
        cachedFile: CachedFile
    ): NullableBook
}