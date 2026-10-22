/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ua.acclorite.book_story.data.mapper.chapter.ChapterMapperImpl
import ua.acclorite.book_story.data.storage.room.entity.content.ChapterEntity
import ua.acclorite.book_story.domain.model.book.Chapter
import ua.acclorite.book_story.domain.model.book.ChapterId
import ua.acclorite.book_story.domain.model.book.ChapterPosition
import ua.acclorite.book_story.domain.model.book.LinkId
import kotlin.test.assertFailsWith

class ChapterMapperImplTest {

    private val chapterMapper = ChapterMapperImpl()

    @Test
    fun `Chapter should survive round trip`() {
        val chapter = Chapter(
            id = ChapterId(5),
            linkId = LinkId(2),
            parentId = ChapterId(4),
            title = "Chapter title",
            position = ChapterPosition(
                chapterOffset = 5,
                endChapterOffset = 8,
                chapterLength = 5
            )
        )

        val chapterEntity = chapterMapper.toChapterEntity(chapter)
        val result = chapterMapper.toChapter(chapterEntity)

        assertEquals(chapter, result)
    }

    @Test
    fun `Incorrect Chapter should throw`() {
        val chapter = Chapter(
            id = ChapterId(5),
            linkId = LinkId(2),
            parentId = ChapterId(5), // same parentId as chapterId
            title = "Chapter title",
            position = ChapterPosition(
                chapterOffset = 5,
                endChapterOffset = 8,
                chapterLength = 5
            )
        )

        val exception = assertFailsWith<IllegalStateException> {
            chapterMapper.toChapterEntity(chapter)
        }

        assertEquals("Incorrect Chapter instance.", exception.message)
    }

    @Test
    fun `Incorrect ChapterEntity should throw`() {
        val chapterEntity = ChapterEntity(
            id = 5,
            linkId = 2,
            parentId = 4,
            title = "Chapter title",
            chapterOffset = 4,
            endChapterOffset = 3, // End chapter offset is less than start offset
            chapterLength = 1
        )

        val exception = assertFailsWith<IllegalStateException> {
            chapterMapper.toChapter(chapterEntity)
        }

        assertEquals("Incorrect ChapterEntity instance.", exception.message)
    }
}