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
import ua.acclorite.book_story.data.storage.room.entity.content.NodeEntity

@Dao
interface NodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNodes(nodes: List<NodeEntity>)

    @Query(
        """
    SELECT * FROM (
        -- Before target node
        SELECT * FROM nodeentity 
        WHERE id < :nodeId 
        ORDER BY id DESC 
        LIMIT :range
    )
    UNION ALL
    SELECT * FROM (
        -- Target node itself
        SELECT * FROM nodeentity 
        WHERE id = :nodeId
    )
    UNION ALL
    SELECT * FROM (
        -- After target node
        SELECT * FROM nodeentity 
        WHERE id > :nodeId 
        ORDER BY id ASC 
        LIMIT :range
    )
    ORDER BY id ASC
    """
    )
    suspend fun getNodesInRange(nodeId: Int, range: Int): List<NodeEntity>

    @Query("SELECT * FROM nodeentity WHERE id > :nodeId ORDER BY id ASC LIMIT :limit")
    suspend fun getNodesAfter(nodeId: Int, limit: Int): List<NodeEntity>

    @Query("SELECT * FROM (SELECT * FROM nodeentity WHERE id < :nodeId ORDER BY id DESC LIMIT :limit) ORDER BY id ASC")
    suspend fun getNodesBefore(nodeId: Int, limit: Int): List<NodeEntity>

    @Query("SELECT * FROM nodeentity WHERE offset <= :offset ORDER BY offset DESC LIMIT 1")
    suspend fun getNodeByOffset(offset: Int): NodeEntity?

    @Query("SELECT NOT EXISTS (SELECT 1 FROM nodeentity)")
    fun isEmpty(): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM nodeentity)")
    fun isNotEmpty(): Boolean

    @Query("DELETE FROM nodeentity")
    fun clear()
}
