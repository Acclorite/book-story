package com.acclorite.books_history.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.acclorite.books_history.data.local.dto.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract val dao: BookDao
}