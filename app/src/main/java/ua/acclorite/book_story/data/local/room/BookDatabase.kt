/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.local.room

import android.app.Application
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.data.local.dto.CategoryEntity
import ua.acclorite.book_story.data.local.dto.ColorPresetEntity
import ua.acclorite.book_story.data.local.dto.HistoryEntity
import java.io.File

@Database(
    entities = [
        BookEntity::class,
        HistoryEntity::class,
        ColorPresetEntity::class,
        CategoryEntity::class
    ],
    version = 16,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
        AutoMigration(3, 4, spec = DatabaseHelper.AUTO_MIGRATION_3_4::class),
        AutoMigration(4, 5),
        AutoMigration(5, 6),
        AutoMigration(6, 7),
        AutoMigration(7, 8, spec = DatabaseHelper.AUTO_MIGRATION_7_8::class),
        AutoMigration(8, 9, spec = DatabaseHelper.AUTO_MIGRATION_8_9::class),
        AutoMigration(9, 10, spec = DatabaseHelper.AUTO_MIGRATION_9_10::class),
        AutoMigration(10, 11),
        AutoMigration(11, 12),
        AutoMigration(12, 13),
        AutoMigration(13, 14),
        AutoMigration(14, 15),
        AutoMigration(15, 16, spec = DatabaseHelper.AUTO_MIGRATION_15_16::class),
    ],
    exportSchema = true
)
abstract class BookDatabase : RoomDatabase() {
    abstract val bookDao: BookDao
    abstract val historyDao: HistoryDao
    abstract val colorPresetDao: ColorPresetDao
    abstract val categoryDao: CategoryDao
}

@Suppress("ClassName")
object DatabaseHelper {

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
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
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
            database.execSQL(
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
            database.execSQL("DROP TABLE ColorPresetEntity")
            database.execSQL("ALTER TABLE ColorPresetEntity_new RENAME TO ColorPresetEntity")
        }
    }

    val MANUAL_MIGRATION_14_15 = object : Migration(14, 15) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
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
            database.execSQL(
                """
                    INSERT INTO BookEntity_new (id, title, author, description, filePath, scrollIndex, scrollOffset, progress, image, categories)
                    SELECT id, title, COALESCE(author, ''), description, filePath, scrollIndex, scrollOffset, progress, image, categories
                    FROM BookEntity
                """
            )
            database.execSQL("DROP TABLE BookEntity")
            database.execSQL("ALTER TABLE BookEntity_new RENAME TO BookEntity")
        }
    }

    @DeleteTable("CategorySortEntity")
    class AUTO_MIGRATION_15_16 : AutoMigrationSpec

    val MANUAL_MIGRATION_15_16 = object : Migration(15, 16) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
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
            database.execSQL(
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
            database.execSQL(
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
            database.execSQL("DROP TABLE CategoryEntity")
            database.execSQL("DROP TABLE CategorySortEntity")
            database.execSQL("ALTER TABLE CategoryEntity_new RENAME TO CategoryEntity")
        }
    }
}