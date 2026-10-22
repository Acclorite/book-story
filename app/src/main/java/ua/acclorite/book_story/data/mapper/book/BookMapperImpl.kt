/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.book

import androidx.core.net.toUri
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.storage.room.entity.BookEntity
import ua.acclorite.book_story.domain.model.book.Book
import ua.acclorite.book_story.domain.model.book.BookProgress
import ua.acclorite.book_story.domain.model.book.NodeId
import javax.inject.Inject

class BookMapperImpl @Inject constructor() : BookMapper {
    override fun toBookEntity(book: Book): BookEntity {
        return BookEntity(
            id = book.id,
            uuid = book.uuid,
            title = book.title,
            filePath = book.filePath,
            currentNodeId = book.progress.currentNodeId.id,
            currentNodeIntraOffset = book.progress.currentNodeIntraOffset,
            totalNodes = book.progress.totalNodes,
            currentOffset = book.progress.currentOffset,
            totalLength = book.progress.totalLength,
            author = book.author.getAsString() ?: "",
            description = book.description,
            image = book.coverImage?.toString(),
            categories = book.categories,
            scrollIndex = book.scrollIndex,
            scrollOffset = book.scrollOffset,
            progress = book.oldProgress
        )
    }

    override fun toBook(bookEntity: BookEntity): Book {
        return Book(
            id = bookEntity.id,
            uuid = bookEntity.uuid,
            title = bookEntity.title,
            author = bookEntity.author.let { author ->
                if (author.isNotBlank()) UIText.StringValue(author)
                else UIText.StringResource(R.string.unknown_author)
            },
            description = bookEntity.description,
            progress = BookProgress(
                currentNodeId = NodeId(bookEntity.currentNodeId),
                currentNodeIntraOffset = bookEntity.currentNodeIntraOffset,
                totalNodes = bookEntity.totalNodes,
                currentOffset = bookEntity.currentOffset,
                totalLength = bookEntity.totalLength
            ),
            filePath = bookEntity.filePath,
            lastOpened = null,
            coverImage = bookEntity.image?.toUri(),
            categories = bookEntity.categories,
            scrollIndex = bookEntity.scrollIndex,
            scrollOffset = bookEntity.scrollOffset,
            oldProgress = bookEntity.progress
        )
    }
}