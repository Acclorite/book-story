/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.chapter

import ua.acclorite.book_story.data.storage.room.entity.content.ChapterEntity
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.ChapterId
import ua.acclorite.book_story.domain.model.book.ChapterPosition
import ua.acclorite.book_story.domain.model.book.LinkId
import javax.inject.Inject

class ChapterMapperImpl @Inject constructor() : ChapterMapper {
    override fun toChapterEntity(chapter: Chapter): ChapterEntity {
        if (chapter.id.id <= -1
            || chapter.title.isBlank()
            || chapter.position.chapterLength <= 0
            || chapter.position.endChapterOffset < chapter.position.chapterOffset
            || chapter.parentId == chapter.id
            || chapter.linkId.id <= -1
        ) {
            throw IllegalStateException("Incorrect Chapter instance.")
        }

        return ChapterEntity(
            id = chapter.id.id,
            linkId = chapter.linkId.id,
            title = chapter.title,
            parentId = chapter.parentId?.id,
            chapterOffset = chapter.position.chapterOffset,
            endChapterOffset = chapter.position.endChapterOffset,
            chapterLength = chapter.position.chapterLength
        )
    }

    override fun toChapter(chapterEntity: ChapterEntity): Chapter {
        if (chapterEntity.id <= -1
            || chapterEntity.title.isBlank()
            || chapterEntity.chapterLength <= 0
            || chapterEntity.endChapterOffset < chapterEntity.chapterOffset
            || chapterEntity.parentId == chapterEntity.id
            || chapterEntity.linkId <= -1
        ) {
            throw IllegalStateException("Incorrect ChapterEntity instance.")
        }

        return Chapter(
            id = ChapterId(chapterEntity.id),
            linkId = LinkId(chapterEntity.linkId),
            parentId = chapterEntity.parentId?.let { ChapterId(it) },
            title = chapterEntity.title,
            position = ChapterPosition(
                chapterOffset = chapterEntity.chapterOffset,
                endChapterOffset = chapterEntity.endChapterOffset,
                chapterLength = chapterEntity.chapterLength
            )
        )
    }
}