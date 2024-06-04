package ua.acclorite.book_story.data.di

import android.app.Application
import androidx.room.Room
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.languageid.LanguageIdentifier
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
        ).allowMainThreadQueries().build().dao
    }

    @Provides
    @Singleton
    fun provideModelManager(): RemoteModelManager {
        return RemoteModelManager.getInstance()
    }

    @Provides
    @Singleton
    fun provideLanguageIdentifier(): LanguageIdentifier {
        return LanguageIdentification.getClient(
            LanguageIdentificationOptions.Builder()
                .setConfidenceThreshold(0.2f)
                .build()
        )
    }
}