package ua.acclorite.book_story.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.data.local.dto.ColorPresetEntity
import ua.acclorite.book_story.data.local.dto.FavoriteDirectoryEntity
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
    suspend fun findBookById(id: Int): BookEntity

    @Query("SELECT * FROM bookentity WHERE id IN (:ids)")
    suspend fun findBooksById(ids: List<Int>): List<BookEntity>

    @Delete
    suspend fun deleteBooks(books: List<BookEntity>)

    @Update
    suspend fun updateBooks(books: List<BookEntity>)
    /* - - - - - - - - - - - - - - - - - - - - - - */


    /* ------ HistoryEntity --------------------- */
    @Query("SELECT * FROM historyentity")
    suspend fun getHistory(): List<HistoryEntity>

    @Query("SELECT * FROM historyentity WHERE bookId = :bookId ORDER BY time DESC LIMIT 1")
    fun getLatestHistoryForBook(bookId: Int): HistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(
        history: List<HistoryEntity>
    )

    @Query("DELETE FROM historyentity")
    suspend fun deleteWholeHistory()

    @Query("DELETE FROM historyentity WHERE bookId = :bookId")
    suspend fun deleteBookHistory(bookId: Int)

    @Delete
    suspend fun deleteHistory(history: List<HistoryEntity>)
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


    /* ------ FavoriteDirectoryEntity ----------- */
    @Upsert
    suspend fun insertFavoriteDirectory(favoriteDirectoryEntity: FavoriteDirectoryEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favoritedirectoryentity WHERE path = :path LIMIT 1)")
    suspend fun favoriteDirectoryExits(path: String): Boolean

    @Delete
    suspend fun deleteFavoriteDirectory(favoriteDirectoryEntity: FavoriteDirectoryEntity)
    /* - - - - - - - - - - - - - - - - - - - - - - */
}