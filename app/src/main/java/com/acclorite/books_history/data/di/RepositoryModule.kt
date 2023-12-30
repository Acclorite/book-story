package com.acclorite.books_history.data.di

import com.acclorite.books_history.data.local.data_store.DataStore
import com.acclorite.books_history.data.local.data_store.DataStoreImpl
import com.acclorite.books_history.data.repository.BookRepositoryImpl
import com.acclorite.books_history.domain.repository.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}