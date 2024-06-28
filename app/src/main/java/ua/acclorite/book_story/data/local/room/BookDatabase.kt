package ua.acclorite.book_story.data.local.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.data.local.dto.HistoryEntity
import ua.acclorite.book_story.data.local.dto.LanguageHistoryEntity

@Database(
    entities = [
        BookEntity::class,
        HistoryEntity::class,
        LanguageHistoryEntity::class
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
    ],
    exportSchema = true
)
abstract class BookDatabase : RoomDatabase() {
    abstract val dao: BookDao
}

object DatabaseHelper {

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `LanguageHistoryEntity` (" +
                        "`languageCode` TEXT NOT NULL," +
                        " `order` INTEGER NOT NULL," +
                        " PRIMARY KEY(`languageCode`)" +
                        ")"
            )
        }
    }
}