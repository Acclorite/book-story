/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser

import ua.acclorite.book_story.data.model.file.CachedFile
import ua.acclorite.book_story.domain.model.reader.ReaderText

interface TextParser {
    suspend fun parse(cachedFile: CachedFile): List<ReaderText>
}