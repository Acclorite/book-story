/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.use_case.file_system

import ua.blindmint.codex.domain.file.CachedFile
import ua.blindmint.codex.domain.library.book.NullableBook
import ua.blindmint.codex.domain.repository.FileSystemRepository
import javax.inject.Inject

class GetBookFromFile @Inject constructor(
    private val repository: FileSystemRepository
) {

    suspend fun execute(cachedFile: CachedFile): NullableBook {
        return repository.getBookFromFile(cachedFile)
    }
}