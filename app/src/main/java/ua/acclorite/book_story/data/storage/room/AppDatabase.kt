/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.storage.room

import android.app.Application
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.acclorite.book_story.data.storage.room.dao.BookDao
import ua.acclorite.book_story.data.storage.room.dao.CategoryDao
import ua.acclorite.book_story.data.storage.room.dao.ColorPresetDao
import ua.acclorite.book_story.data.storage.room.dao.HistoryDao
import ua.acclorite.book_story.data.storage.room.entity.BookEntity
import ua.acclorite.book_story.data.storage.room.entity.CategoryEntity
import ua.acclorite.book_story.data.storage.room.entity.ColorPresetEntity
import ua.acclorite.book_story.data.storage.room.entity.HistoryEntity
import java.io.File
import java.util.UUID

@Database(
    entities = [
        BookEntity::class,
        HistoryEntity::class,
        ColorPresetEntity::class,
        CategoryEntity::class
    ],
    version = 18,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
        AutoMigration(3, 4, spec = AppDatabaseMigrationHelper.AUTO_MIGRATION_3_4::class),
        AutoMigration(4, 5),
        AutoMigration(5, 6),
        AutoMigration(6, 7),
        AutoMigration(7, 8, spec = AppDatabaseMigrationHelper.AUTO_MIGRATION_7_8::class),
        AutoMigration(8, 9, spec = AppDatabaseMigrationHelper.AUTO_MIGRATION_8_9::class),
        AutoMigration(9, 10, spec = AppDatabaseMigrationHelper.AUTO_MIGRATION_9_10::class),
        AutoMigration(10, 11),
        AutoMigration(11, 12),
        AutoMigration(12, 13),
        AutoMigration(13, 14),
        AutoMigration(14, 15),
        AutoMigration(15, 16, spec = AppDatabaseMigrationHelper.AUTO_MIGRATION_15_16::class),
        AutoMigration(16, 17),
        AutoMigration(17, 18)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "app-database.db"

        fun buildDatabase(application: Application): AppDatabase {
            /* - Additional Migrations - */
            AppDatabaseMigrationHelper.AUTO_MIGRATION_7_8.removeBooksDir(application)
            AppDatabaseMigrationHelper.databaseNameMigration(application)
            val callback = object : Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        // Ensure default category
                        db.execSQL(
                            """
                            INSERT OR IGNORE INTO CategoryEntity (
                                id, title, 
                                `order`, 
                                sortOrder, 
                                sortOrderDescending
                            ) VALUES (-1, '', -1, 'LAST_READ', 1)
                        """
                        )
                    }
                }
            }
            /* - - - - - - - - - - - - - */

            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                DATABASE_NAME
            ).addMigrations(
                AppDatabaseMigrationHelper.MANUAL_MIGRATION_2_3, // creates LanguageHistoryEntity table (if it does not exist)
                AppDatabaseMigrationHelper.MANUAL_MIGRATION_4_5, // creates ColorPresetEntity table (if it does not exist)
                AppDatabaseMigrationHelper.MANUAL_MIGRATION_5_6, // creates FavoriteDirectoryEntity table (if it does not exist)
                AppDatabaseMigrationHelper.MANUAL_MIGRATION_13_14, // remove nullability from ColorPresetEntity
                AppDatabaseMigrationHelper.MANUAL_MIGRATION_14_15, // remove author nullability from BookEntity
                AppDatabaseMigrationHelper.MANUAL_MIGRATION_15_16, // merge CategoryEntity and CategorySortEntity
                AppDatabaseMigrationHelper.MANUAL_MIGRATION_16_17, // add uuid field to BookEntity
            ).addCallback(callback).build()
        }
    }

    abstract val bookDao: BookDao
    abstract val historyDao: HistoryDao
    abstract val colorPresetDao: ColorPresetDao
    abstract val categoryDao: CategoryDao
}

@Suppress("ClassName")
object AppDatabaseMigrationHelper {
    val MANUAL_MIGRATION_2_3 = object : Migration(2, 3) {
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

    @DeleteColumn("BookEntity", "enableTranslator")
    @DeleteColumn("BookEntity", "translateFrom")
    @DeleteColumn("BookEntity", "translateTo")
    @DeleteColumn("BookEntity", "doubleClickTranslation")
    @DeleteColumn("BookEntity", "translateWhenOpen")
    @DeleteTable("LanguageHistoryEntity")
    class AUTO_MIGRATION_3_4 : AutoMigrationSpec

    val MANUAL_MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `ColorPresetEntity` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`name` TEXT, " +
                        "`backgroundColor` INTEGER NOT NULL, " +
                        "`fontColor` INTEGER NOT NULL, " +
                        "`isSelected` INTEGER NOT NULL, " +
                        "`order` INTEGER NOT NULL" +
                        ")"
            )
        }
    }

    val MANUAL_MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `FavoriteDirectoryEntity` (" +
                        "`path` TEXT NOT NULL, " +
                        "PRIMARY KEY(`path`)" +
                        ")"
            )
        }
    }

    @DeleteColumn("BookEntity", "textPath")
    @DeleteColumn("BookEntity", "chapters")
    class AUTO_MIGRATION_7_8 : AutoMigrationSpec {
        companion object {
            /**
             * Along with textPath deletion,
             * books directory with text does not
             * serve any purpose.
             */
            fun removeBooksDir(application: Application) {
                val booksDir = File(application.filesDir, "books")

                if (booksDir.exists()) {
                    booksDir.deleteRecursively()
                }
            }
        }
    }

    @DeleteTable("FavoriteDirectoryEntity")
    class AUTO_MIGRATION_8_9 : AutoMigrationSpec

    @DeleteColumn("BookEntity", "category")
    class AUTO_MIGRATION_9_10 : AutoMigrationSpec

    val MANUAL_MIGRATION_13_14 = object : Migration(13, 14) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                    CREATE TABLE IF NOT EXISTS ColorPresetEntity_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        backgroundColor INTEGER NOT NULL,
                        fontColor INTEGER NOT NULL,
                        isSelected INTEGER NOT NULL,
                        `order` INTEGER NOT NULL
                    )
                """
            )
            db.execSQL(
                """
                    INSERT INTO ColorPresetEntity_new (id, name, backgroundColor, fontColor, isSelected, `order`)
                    SELECT 
                        id,
                        COALESCE(name, ''),
                        backgroundColor,
                        fontColor,
                        isSelected,
                        `order`
                    FROM ColorPresetEntity
                """
            )
            db.execSQL("DROP TABLE ColorPresetEntity")
            db.execSQL("ALTER TABLE ColorPresetEntity_new RENAME TO ColorPresetEntity")
        }
    }

    val MANUAL_MIGRATION_14_15 = object : Migration(14, 15) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                    CREATE TABLE BookEntity_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        author TEXT NOT NULL,
                        description TEXT,
                        filePath TEXT NOT NULL,
                        scrollIndex INTEGER NOT NULL,
                        scrollOffset INTEGER NOT NULL,
                        progress REAL NOT NULL,
                        image TEXT,
                        categories TEXT NOT NULL DEFAULT '[]'
                    )
                """
            )
            db.execSQL(
                """
                    INSERT INTO BookEntity_new (id, title, author, description, filePath, scrollIndex, scrollOffset, progress, image, categories)
                    SELECT id, title, COALESCE(author, ''), description, filePath, scrollIndex, scrollOffset, progress, image, categories
                    FROM BookEntity
                """
            )
            db.execSQL("DROP TABLE BookEntity")
            db.execSQL("ALTER TABLE BookEntity_new RENAME TO BookEntity")
        }
    }

    @DeleteTable("CategorySortEntity")
    class AUTO_MIGRATION_15_16 : AutoMigrationSpec

    val MANUAL_MIGRATION_15_16 = object : Migration(15, 16) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                    CREATE TABLE IF NOT EXISTS CategoryEntity_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        `order` INTEGER NOT NULL,
                        sortOrder TEXT NOT NULL DEFAULT 'LAST_READ',
                        sortOrderDescending INTEGER NOT NULL DEFAULT 1
                    )
                """
            )
            db.execSQL(
                """
                    INSERT INTO CategoryEntity_new (id, title, `order`, sortOrder, sortOrderDescending)
                    SELECT 
                        c.id,
                        c.title,
                        c.`order`,
                        COALESCE(cs.sortOrder, 'LAST_READ'),
                        COALESCE(cs.sortOrderDescending, 1)
                    FROM CategoryEntity c
                    LEFT JOIN CategorySortEntity cs
                        ON c.id = cs.categoryId
                """
            )
            db.execSQL(
                """
                    INSERT INTO CategoryEntity_new (id, title, `order`, sortOrder, sortOrderDescending)
                    SELECT 
                        -1,
                        '',
                        -1,
                        COALESCE(cs.sortOrder, 'LAST_READ'),
                        COALESCE(cs.sortOrderDescending, 1)
                    FROM CategorySortEntity cs
                    WHERE cs.categoryId = -1
                      AND NOT EXISTS (
                        SELECT 1 FROM CategoryEntity WHERE id = -1
                      )
                """
            )
            db.execSQL("DROP TABLE CategoryEntity")
            db.execSQL("DROP TABLE CategorySortEntity")
            db.execSQL("ALTER TABLE CategoryEntity_new RENAME TO CategoryEntity")
        }
    }

    fun databaseNameMigration(application: Application) {
        val databaseDir = File(application.dataDir, "databases")
        if (!databaseDir.exists()) return

        File(databaseDir, "book_db").let { databaseFile ->
            if (databaseFile.exists()) {
                val newDatabaseFile = File(databaseDir, "app-database.db")
                databaseFile.renameTo(newDatabaseFile)
            }
        }
        File(databaseDir, "book_db-wal").let { databaseWalFile ->
            if (databaseWalFile.exists()) {
                val newDatabaseWalFile = File(databaseDir, "app-database.db-wal")
                databaseWalFile.renameTo(newDatabaseWalFile)
            }
        }
        File(databaseDir, "book_db-shm").let { databaseShmFile ->
            if (databaseShmFile.exists()) {
                val newDatabaseShmFile = File(databaseDir, "app-database.db-shm")
                databaseShmFile.renameTo(newDatabaseShmFile)
            }
        }
    }

    val MANUAL_MIGRATION_16_17 = object : Migration(16, 17) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE BookEntity ADD COLUMN uuid TEXT NOT NULL DEFAULT ''")
            db.query("SELECT id FROM BookEntity").use { cursor ->
                val idIndex = cursor.getColumnIndexOrThrow("id")
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(idIndex)
                    db.execSQL(
                        "UPDATE BookEntity SET uuid = ? WHERE id = ?",
                        arrayOf<Any>(UUID.randomUUID().toString(), id)
                    )
                }
            }
        }
    }
}