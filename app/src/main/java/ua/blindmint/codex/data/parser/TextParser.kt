/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.parser

import ua.blindmint.codex.domain.file.CachedFile
import ua.blindmint.codex.domain.reader.ReaderText

interface TextParser {

    suspend fun parse(cachedFile: CachedFile): List<ReaderText>
}