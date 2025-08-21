/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.core.helpers.coerceAndPreventNaN
import ua.acclorite.book_story.domain.model.reader.ReaderText
import ua.acclorite.book_story.domain.model.reader.ReaderText.Chapter
import javax.inject.Inject

class GetChapterProgressUseCase @Inject constructor() {

    operator fun invoke(index: Int, text: List<ReaderText>): Pair<Chapter?, Float> {
        fun getCurrentChapter(index: Int): Chapter? {
            for (textIndex in index downTo 0) {
                val readerText = text.getOrNull(textIndex) ?: break
                if (readerText is Chapter) {
                    return readerText
                }
            }
            return null
        }

        fun getCurrentChapterProgress(currentChapter: Chapter?): Float {
            return currentChapter?.let { currentChapter ->
                val startIndex = text.indexOf(currentChapter).coerceIn(0, text.lastIndex)
                val endIndex = (text.indexOfFirst {
                    it is Chapter && text.indexOf(it) > startIndex
                }.takeIf { it != -1 }) ?: (text.lastIndex + 1)

                val currentIndexInChapter = (index - startIndex).coerceAtLeast(1)
                val chapterLength = endIndex - (startIndex + 1)
                (currentIndexInChapter / chapterLength.toFloat())
            }.coerceAndPreventNaN()
        }

        val currentChapter = getCurrentChapter(index)
        val currentChapterProgress = getCurrentChapterProgress(currentChapter)

        return currentChapter to currentChapterProgress
    }
}