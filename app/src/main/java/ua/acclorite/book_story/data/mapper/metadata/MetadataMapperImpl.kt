/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.metadata

import ua.acclorite.book_story.data.storage.room.entity.content.MetadataEntity
import ua.acclorite.book_story.domain.model.book.Metadata
import ua.acclorite.book_story.domain.service.FileHash
import javax.inject.Inject

class MetadataMapperImpl @Inject constructor() : MetadataMapper {
    override fun toMetadataEntity(metadata: Metadata): MetadataEntity {
        if (metadata.id <= -1
            || metadata.parser.isBlank()
            || metadata.fileHash.hash.isBlank()
            || metadata.parserVersion <= 0
        ) {
            throw IllegalStateException("Incorrect Metadata instance.")
        }

        return MetadataEntity(
            id = metadata.id,
            fileHash = metadata.fileHash.hash,
            parserVersion = metadata.parserVersion,
            parser = metadata.parser
        )
    }

    override fun toMetadata(metadataEntity: MetadataEntity): Metadata {
        if (metadataEntity.id <= -1
            || metadataEntity.parser.isBlank()
            || metadataEntity.fileHash.isBlank()
            || metadataEntity.parserVersion <= 0
        ) {
            throw IllegalStateException("Incorrect MetadataEntity instance.")
        }

        return Metadata(
            fileHash = FileHash(metadataEntity.fileHash),
            parserVersion = metadataEntity.parserVersion,
            parser = metadataEntity.parser
        )
    }
}