package ua.acclorite.book_story.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.acclorite.book_story.data.local.data_store.DataStore
import ua.acclorite.book_story.data.local.data_store.DataStoreImpl
import ua.acclorite.book_story.data.mapper.BookMapper
import ua.acclorite.book_story.data.mapper.BookMapperImpl
import ua.acclorite.book_story.data.mapper.HistoryMapper
import ua.acclorite.book_story.data.mapper.HistoryMapperImpl
import ua.acclorite.book_story.data.repository.BookRepositoryImpl
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDataStore(
        dataStoreImpl: DataStoreImpl
    ): DataStore

    @Binds
    @Singleton
    abstract fun bindBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository

    @Binds
    @Singleton
    abstract fun bindBookMapper(
        bookMapperImpl: BookMapperImpl
    ): BookMapper

    @Binds
    @Singleton
    abstract fun bindHistoryMapper(
        historyMapperImpl: HistoryMapperImpl
    ): HistoryMapper
}