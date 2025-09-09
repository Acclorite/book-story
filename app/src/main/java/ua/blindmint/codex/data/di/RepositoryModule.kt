/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.blindmint.codex.data.local.data_store.DataStore
import ua.blindmint.codex.data.local.data_store.DataStoreImpl
import ua.blindmint.codex.data.mapper.book.BookMapper
import ua.blindmint.codex.data.mapper.book.BookMapperImpl
import ua.blindmint.codex.data.mapper.color_preset.ColorPresetMapper
import ua.blindmint.codex.data.mapper.color_preset.ColorPresetMapperImpl
import ua.blindmint.codex.data.mapper.history.HistoryMapper
import ua.blindmint.codex.data.mapper.history.HistoryMapperImpl
import ua.blindmint.codex.data.parser.FileParser
import ua.blindmint.codex.data.parser.FileParserImpl
import ua.blindmint.codex.data.parser.TextParser
import ua.blindmint.codex.data.parser.TextParserImpl
import ua.blindmint.codex.data.repository.BookRepositoryImpl
import ua.blindmint.codex.data.repository.ColorPresetRepositoryImpl
import ua.blindmint.codex.data.repository.DataStoreRepositoryImpl
import ua.blindmint.codex.data.repository.FileSystemRepositoryImpl
import ua.blindmint.codex.data.repository.HistoryRepositoryImpl
import ua.blindmint.codex.data.repository.PermissionRepositoryImpl
import ua.blindmint.codex.domain.repository.BookRepository
import ua.blindmint.codex.domain.repository.ColorPresetRepository
import ua.blindmint.codex.domain.repository.DataStoreRepository
import ua.blindmint.codex.domain.repository.FileSystemRepository
import ua.blindmint.codex.domain.repository.HistoryRepository
import ua.blindmint.codex.domain.repository.PermissionRepository
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