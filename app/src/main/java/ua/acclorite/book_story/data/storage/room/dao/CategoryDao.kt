/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ua.acclorite.book_story.data.storage.room.entity.CategoryEntity

@Dao
interface CategoryDao {
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