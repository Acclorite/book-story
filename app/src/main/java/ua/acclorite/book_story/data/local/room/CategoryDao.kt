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
import androidx.room.Update
import ua.acclorite.book_story.data.local.dto.CategoryEntity

@Dao
interface CategoryDao {
    @Query("INSERT OR IGNORE INTO CategoryEntity (id, title, `order`, sortOrder, sortOrderDescending) VALUES (-1, '', -1, 'LAST_READ', 1)")
    fun ensureDefaultCategory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(
        category: CategoryEntity
    )

    @Query("SELECT * FROM categoryentity ORDER BY `order` ASC")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("SELECT COUNT(*) FROM categoryentity")
    suspend fun getCategoriesCount(): Int

    @Update
    suspend fun updateCategory(
        category: CategoryEntity
    )

    @Delete
    suspend fun deleteCategory(
        category: CategoryEntity
    )
}