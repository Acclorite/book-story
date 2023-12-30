package com.acclorite.books_history.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.acclorite.books_history.data.local.room.BookDao
import com.acclorite.books_history.data.local.room.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun provideBookDao(app: Application): BookDao {
        return Room.databaseBuilder(
            app,
            BookDatabase::class.java,
            "book_db"
        ).build().dao
    }
}