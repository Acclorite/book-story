/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ua.acclorite.book_story.data.mapper.metadata.MetadataMapperImpl
import ua.acclorite.book_story.data.storage.room.entity.content.MetadataEntity
import ua.acclorite.book_story.domain.model.book.Metadata
import ua.acclorite.book_story.domain.service.FileHash
import kotlin.test.assertFailsWith

class MetadataMapperImplTest {

    private val metadataMapper = MetadataMapperImpl()

    @Test
    fun `Metadata should survive round trip`() {
        val metadata = Metadata(
            fileHash = FileHash("gsfdhj34587gdfhg"),
            parserVersion = 4,
            parser = "xml"
        )

        val metadataEntity = metadataMapper.toMetadataEntity(metadata)
        val result = metadataMapper.toMetadata(metadataEntity)

        assertEquals(metadata, result)
    }

    @Test
    fun `Incorrect Metadata should throw`() {
        val metadata = Metadata(
            fileHash = FileHash("gsfdhj34587gdfhg"),
            parserVersion = 4,
            parser = " " // Blank parser
        )

        val exception = assertFailsWith<IllegalStateException> {
            metadataMapper.toMetadataEntity(metadata)
        }

        assertEquals("Incorrect Metadata instance.", exception.message)
    }

    @Test
    fun `Incorrect MetadataEntity should throw`() {
        val metadata = MetadataEntity(
            id = 5,
            fileHash = "", // Blank file hash
            parserVersion = 4,
            parser = "epub"
        )

        val exception = assertFailsWith<IllegalStateException> {
            metadataMapper.toMetadata(metadata)
        }

        assertEquals("Incorrect MetadataEntity instance.", exception.message)
    }
}