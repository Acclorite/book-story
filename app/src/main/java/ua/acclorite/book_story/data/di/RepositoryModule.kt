/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.acclorite.book_story.data.local.data_store.DataStore
import ua.acclorite.book_story.data.local.data_store.DataStoreImpl
import ua.acclorite.book_story.data.mapper.book.BookMapper
import ua.acclorite.book_story.data.mapper.book.BookMapperImpl
import ua.acclorite.book_story.data.mapper.color_preset.ColorPresetMapper
import ua.acclorite.book_story.data.mapper.color_preset.ColorPresetMapperImpl
import ua.acclorite.book_story.data.mapper.history.HistoryMapper
import ua.acclorite.book_story.data.mapper.history.HistoryMapperImpl
import ua.acclorite.book_story.data.parser.FileParser
import ua.acclorite.book_story.data.parser.FileParserImpl
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.data.parser.TextParserImpl
import ua.acclorite.book_story.data.repository.BookRepositoryImpl
import ua.acclorite.book_story.data.repository.ColorPresetRepositoryImpl
import ua.acclorite.book_story.data.repository.DataStoreRepositoryImpl
import ua.acclorite.book_story.data.repository.FileSystemRepositoryImpl
import ua.acclorite.book_story.data.repository.HistoryRepositoryImpl
import ua.acclorite.book_story.data.repository.PermissionRepositoryImpl
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.repository.ColorPresetRepository
import ua.acclorite.book_story.domain.repository.DataStoreRepository
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import ua.acclorite.book_story.domain.repository.HistoryRepository
import ua.acclorite.book_story.domain.repository.PermissionRepository
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
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindColorPresetRepository(
        colorPresetRepositoryImpl: ColorPresetRepositoryImpl
    ): ColorPresetRepository

    @Binds
    @Singleton
    abstract fun bindDataStoreRepository(
        dataStoreRepositoryImpl: DataStoreRepositoryImpl
    ): DataStoreRepository

    @Binds
    @Singleton
    abstract fun bindFileSystemRepository(
        fileSystemRepositoryImpl: FileSystemRepositoryImpl
    ): FileSystemRepository

    @Binds
    @Singleton
    abstract fun bindPermissionRepository(
        permissionRepositoryImpl: PermissionRepositoryImpl
    ): PermissionRepository

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
    abstract fun bindColorPresetMapper(
        colorPresetMapperImpl: ColorPresetMapperImpl
    ): ColorPresetMapper

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
}