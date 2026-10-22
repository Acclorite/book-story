/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.chapter

import ua.acclorite.book_story.data.storage.room.entity.content.ChapterEntity
import ua.acclorite.book_story.domain.model.book.Chapter

interface ChapterMapper {
    fun toChapterEntity(chapter: Chapter): ChapterEntity
    fun toChapter(chapterEntity: ChapterEntity): Chapter
}