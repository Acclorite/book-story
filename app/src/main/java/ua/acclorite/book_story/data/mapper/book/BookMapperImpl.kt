/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.book

import androidx.core.net.toUri
import ua.acclorite.book_story.R
import ua.acclorite.book_story.core.ui.UIText
import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.domain.model.library.Book
import javax.inject.Inject

class BookMapperImpl @Inject constructor() : BookMapper {
    override fun toBookEntity(book: Book): BookEntity {
        return BookEntity(
            id = book.id,
            title = book.title,
            filePath = book.filePath,
            scrollIndex = book.scrollIndex,
            scrollOffset = book.scrollOffset,
            progress = book.progress,
            author = book.author.getAsString() ?: "",
            description = book.description,
            image = book.coverImage?.toString(),
            categories = book.categories
        )
    }

    override fun toBook(bookEntity: BookEntity): Book {
        return Book(
            id = bookEntity.id,
            title = bookEntity.title,
            author = bookEntity.author.let { author ->
                if (author.isNotBlank()) UIText.StringValue(author)
                else UIText.StringResource(R.string.unknown_author)
            },
            description = bookEntity.description,
            scrollIndex = bookEntity.scrollIndex,
            scrollOffset = bookEntity.scrollOffset,
            progress = bookEntity.progress,
            filePath = bookEntity.filePath,
            lastOpened = null,
            coverImage = bookEntity.image?.toUri(),
            categories = bookEntity.categories
        )
    }
}