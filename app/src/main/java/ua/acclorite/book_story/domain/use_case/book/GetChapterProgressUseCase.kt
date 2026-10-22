/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.domain.model.book.Chapter
import javax.inject.Inject

class GetChapterProgressUseCase @Inject constructor() {

    operator fun invoke(currentOffset: Int, chapters: List<Chapter>): Pair<Chapter?, Float> {
        val currentChapter = chapters.find { it.position.isCurrent(currentOffset) }
            ?: chapters.lastOrNull { it.position.chapterOffset <= currentOffset }
        val currentChapterProgress = currentChapter?.position?.progress(currentOffset) ?: 0f

        return currentChapter to currentChapterProgress
    }
}