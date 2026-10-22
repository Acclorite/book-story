/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.storage.room.dao.content

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.acclorite.book_story.data.storage.room.entity.content.MetadataEntity

@Dao
interface MetadataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMetadata(
        metadataEntity: MetadataEntity
    )

    @Query("SELECT * FROM metadataentity")
    fun getMetadata(): MetadataEntity?

    @Query("DELETE FROM metadataentity")
    fun clear()
}