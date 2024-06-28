package ua.acclorite.book_story.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.acclorite.book_story.data.local.data_store.DataStore
import ua.acclorite.book_story.data.local.data_store.DataStoreImpl
import ua.acclorite.book_story.data.local.notification.UpdatesNotificationService
import ua.acclorite.book_story.data.local.notification.UpdatesNotificationServiceImpl
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.mapper.book.BookMapperImpl
import ua.acclorite.book_story.data.mapper.history.HistoryMapper
import ua.acclorite.book_story.data.mapper.history.HistoryMapperImpl
import ua.acclorite.book_story.data.mapper.language_history.LanguageHistoryMapper
import ua.acclorite.book_story.data.mapper.language_history.LanguageHistoryMapperImpl
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.data.parser.FileParserImpl
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.data.parser.TextParserImpl
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

    @Binds
    @Singleton
    abstract fun bindLanguageHistoryMapper(
        languageHistoryMapperImpl: LanguageHistoryMapperImpl
    ): LanguageHistoryMapper

    @Binds
    @Singleton
    abstract fun bindFileParser(
        fileParserImpl: FileParserImpl
    ): FileParser

    @Binds
    @Singleton
    abstract fun bindTextParser(
        textParserImpl: TextParserImpl
    ): TextParser

    @Binds
    @Singleton
    abstract fun bindNotificationService(
        updatesNotificationServiceImpl: UpdatesNotificationServiceImpl
    ): UpdatesNotificationService
}