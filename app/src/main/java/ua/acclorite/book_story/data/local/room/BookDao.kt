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
import ua.acclorite.book_story.data.local.dto.ColorPresetEntity
import ua.acclorite.book_story.data.local.dto.HistoryEntity

/**
 * Class to manipulate Room database.
 */
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


    /* ------ HistoryEntity --------------------- */
    @Query("SELECT * FROM historyentity")
    suspend fun getHistory(): List<HistoryEntity>

    @Query("SELECT * FROM historyentity WHERE bookId = :bookId ORDER BY time DESC LIMIT 1")
    fun getHistoryForBook(bookId: Int): HistoryEntity?

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
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ ColorPresetEntity ----------------- */
    @Upsert
    suspend fun updateColorPreset(colorPreset: ColorPresetEntity)

    @Query("SELECT `order` FROM colorpresetentity WHERE :id=id")
    suspend fun getColorPresetOrder(id: Int): Int

    @Query("SELECT COUNT(*) FROM colorpresetentity")
    suspend fun getColorPresetsSize(): Int

    @Query("SELECT * FROM colorpresetentity")
    suspend fun getColorPresets(): List<ColorPresetEntity>

    @Delete
    suspend fun deleteColorPreset(colorPreset: ColorPresetEntity)

    @Query("DELETE FROM colorpresetentity")
    suspend fun deleteColorPresets()
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