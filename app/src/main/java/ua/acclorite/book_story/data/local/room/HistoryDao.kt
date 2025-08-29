/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ua.acclorite.book_story.data.local.dto.HistoryEntity
import ua.acclorite.book_story.data.local.dto.HistoryWithBook

@Dao
interface HistoryDao {
    @Transaction
    @Query("SELECT * FROM historyentity")
    suspend fun getHistoryWithBook(): List<HistoryWithBook>

    @Transaction
    @Query("SELECT * FROM historyentity WHERE bookId = :bookId ORDER BY time DESC LIMIT 1")
    suspend fun getHistoryForBook(bookId: Int): HistoryWithBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(
        history: HistoryEntity
    )

    @Query("DELETE FROM historyentity")
    suspend fun deleteWholeHistory(): Int

    @Query("DELETE FROM historyentity WHERE bookId = :bookId")
    suspend fun deleteHistoryForBook(bookId: Int): Int

    @Delete
    suspend fun deleteHistory(history: HistoryEntity): Int
}