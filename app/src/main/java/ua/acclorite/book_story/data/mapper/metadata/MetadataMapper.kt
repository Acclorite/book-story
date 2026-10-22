/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.metadata

import ua.acclorite.book_story.data.storage.room.entity.content.MetadataEntity
import ua.acclorite.book_story.domain.model.book.Metadata

interface MetadataMapper {
    fun toMetadataEntity(metadata: Metadata): MetadataEntity
    fun toMetadata(metadataEntity: MetadataEntity): Metadata
}