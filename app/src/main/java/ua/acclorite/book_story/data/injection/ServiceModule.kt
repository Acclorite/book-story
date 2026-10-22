/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.injection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.acclorite.book_story.data.service.FileHandlerImpl
import ua.acclorite.book_story.data.service.GZIPCompressionHandlerImpl
import ua.acclorite.book_story.data.service.ImageHandlerImpl
import ua.acclorite.book_story.data.service.NodeHandlerImpl
import ua.acclorite.book_story.domain.service.CompressionHandler
import ua.acclorite.book_story.domain.service.FileHandler
import ua.acclorite.book_story.domain.service.ImageHandler
import ua.acclorite.book_story.domain.service.NodeHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindFileProvider(
        fileHandlerImpl: FileHandlerImpl
    ): FileHandler

    @Binds
    @Singleton
    abstract fun bindCoverImageHandler(
        imageHandlerImpl: ImageHandlerImpl
    ): ImageHandler

    @Binds
    @Singleton
    abstract fun bindCompressionHandler(
        gzipCompressionHandlerImpl: GZIPCompressionHandlerImpl
    ): CompressionHandler

    @Binds
    @Singleton
    abstract fun bindNodeHandler(
        nodeHandlerImpl: NodeHandlerImpl
    ): NodeHandler
}