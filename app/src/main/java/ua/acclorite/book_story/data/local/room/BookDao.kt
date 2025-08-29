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
import androidx.room.Upsert
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.data.local.dto.CategoryEntity
import ua.acclorite.book_story.data.local.dto.CategorySortEntity

@Dao
interface BookDao {

    /* ------ BookEntity ------------------------ */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(
        book: BookEntity
    )

    @Query(
        """
        SELECT * FROM bookentity
        WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%'
    """
    )
    suspend fun searchBooks(query: String): List<BookEntity>

    @Query("SELECT * FROM bookentity WHERE id=:id")
    suspend fun findBookById(id: Int): BookEntity?

    @Delete
    suspend fun deleteBook(book: BookEntity): Int

    @Update
    suspend fun updateBook(book: BookEntity): Int
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ CategoryEntity ----------------- */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(
        category: CategoryEntity
    )

    @Query("SELECT * FROM categoryentity")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("SELECT COUNT(*) FROM categoryentity")
    suspend fun getCategoriesCount(): Int

    @Query("UPDATE categoryentity SET title=:title WHERE id=:id")
    suspend fun updateCategoryTitle(
        id: Int,
        title: String
    )

    @Query("UPDATE categoryentity SET `order`=:order WHERE id=:id")
    suspend fun updateCategoryOrder(
        id: Int,
        order: Int
    )

    @Delete
    suspend fun deleteCategory(
        category: CategoryEntity
    )
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ CategorySortEntity ----------------- */
    @Upsert
    suspend fun updateCategorySort(categorySort: CategorySortEntity)

    @Query("SELECT * FROM categorysortentity")
    suspend fun getCategorySortEntities(): List<CategorySortEntity>

    @Query("DELETE FROM categorysortentity WHERE categoryId=:categoryId")
    suspend fun deleteCategorySortEntity(categoryId: Int)
    /* - - - - - - - - - - - - - - - - - - - - - - */
}