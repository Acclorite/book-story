/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.book

import ua.acclorite.book_story.data.local.dto.BookEntity
import ua.acclorite.book_story.domain.model.library.Book

interface BookMapper {
    fun toBookEntity(book: Book): BookEntity
    fun toBook(bookEntity: BookEntity): Book
}