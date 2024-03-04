package ua.acclorite.book_story.data.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.local.room.BookDatabase
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