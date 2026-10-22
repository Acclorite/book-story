/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.storage.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ua.acclorite.book_story.core.data.FileData
import ua.acclorite.book_story.data.storage.room.dao.content.ChapterDao
import ua.acclorite.book_story.data.storage.room.dao.content.MetadataDao
import ua.acclorite.book_story.data.storage.room.dao.content.NodeDao
import ua.acclorite.book_story.data.storage.room.entity.content.ChapterEntity
import ua.acclorite.book_story.data.storage.room.entity.content.MetadataEntity
import ua.acclorite.book_story.data.storage.room.entity.content.NodeEntity
import java.io.File

@Database(
    entities = [
        NodeEntity::class,
        MetadataEntity::class,
        ChapterEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class ContentDatabase : RoomDatabase() {
    companion object {
        fun buildDatabase(bookUUID: String, application: Application): ContentDatabase {
            val databasePath = FileData.getBookContentDirectory(bookUUID, application)
            File(databasePath).let { if (!it.exists()) it.mkdirs() }

            return Room.databaseBuilder(
                application,
                ContentDatabase::class.java,
                FileData.getBookContentDatabaseFile(bookUUID, application)
            ).fallbackToDestructiveMigration(true).build()
        }
    }

    abstract val metadataDao: MetadataDao
    abstract val nodeDao: NodeDao
    abstract val chapterDao: ChapterDao
}