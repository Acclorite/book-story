package ua.acclorite.book_story.data.local.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.data.local.dto.HistoryEntity

@Database(
    entities = [
        BookEntity::class,
        HistoryEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(1, 2)
    ],
    exportSchema = true
)
abstract class BookDatabase : RoomDatabase() {
    abstract val dao: BookDao
}