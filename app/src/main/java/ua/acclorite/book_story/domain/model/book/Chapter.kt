/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.model.book

import androidx.compose.runtime.Immutable

@Immutable
data class Chapter(
    val id: ChapterId,
    val linkId: LinkId,
    val parentId: ChapterId?,
    val title: String,
    val position: ChapterPosition,
)

val Chapter.cleanTitle: String
    get() = title
        .replace(Regex("<[^>]*>"), "")
        .replace(Regex("\\{\\{.*?\\}\\}"), "")
        .trim()

@JvmInline
value class ChapterId(val id: Int)

@JvmInline
value class LinkId(val id: Int)

/**
 * @property chapterOffset Global chapter offset that marks the start of the chapter
 *                         (including header).
 * @property endChapterOffset Global end chapter offset that marks the end of the whole chapter
 *                            (not only header).
 * @property chapterLength Length of the header only (not the whole chapter).
 */
@Immutable
data class ChapterPosition(
    val chapterOffset: Int,
    val endChapterOffset: Int,
    val chapterLength: Int
) {
    fun isCurrent(offset: Int): Boolean = offset in chapterOffset until endChapterOffset
    fun progress(currentOffset: Int): Float {
        val totalLength = endChapterOffset - chapterOffset
        if (totalLength <= 0) return 0.0f
        return ((currentOffset - chapterOffset).toFloat() / totalLength.toFloat()).coerceIn(
            0.0f,
            1.0f
        )
    }
}
