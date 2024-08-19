package ua.acclorite.book_story.data.di

import android.app.Application
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.local.room.BookDatabase
import ua.acclorite.book_story.data.local.room.DatabaseHelper
import ua.acclorite.book_story.data.remote.GithubAPI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGithubApi(): GithubAPI {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(GithubAPI.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GithubAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideBookDao(app: Application): BookDao {
        return Room.databaseBuilder(
            app,
            BookDatabase::class.java,
            "book_db"
        )
            .addMigrations(
                DatabaseHelper.MIGRATION_2_3, // creates LanguageHistoryEntity table(if does not exist)
                DatabaseHelper.MIGRATION_4_5, // creates ColorPresetEntity table(if does not exist)
                DatabaseHelper.MIGRATION_5_6, // creates FavoriteDirectoryEntity table(if does not exist)
            )
            .allowMainThreadQueries()
            .build()
            .dao
    }
}