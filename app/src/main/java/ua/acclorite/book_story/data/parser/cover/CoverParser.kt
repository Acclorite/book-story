/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.parser.cover

import ua.acclorite.book_story.core.CoverImage
import ua.acclorite.book_story.data.model.file.CachedFile

interface CoverParser {
    suspend fun parse(cachedFile: CachedFile): CoverImage?
}