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
import ua.acclorite.book_story.data.service.CoverImageHandlerImpl
import ua.acclorite.book_story.data.service.FileProviderImpl
import ua.acclorite.book_story.domain.service.CoverImageHandler
import ua.acclorite.book_story.domain.service.FileProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindFileProvider(
        fileProviderImpl: FileProviderImpl
    ): FileProvider

    @Binds
    @Singleton
    abstract fun bindCoverImageHandler(
        coverImageHandlerImpl: CoverImageHandlerImpl
    ): CoverImageHandler
}