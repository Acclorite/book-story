/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.model.book

class HeaderSequence {
    private var previousWasHeader = false
    private var imageOrSeparatorAfterHeader = false

    fun append(content: NodeContent): Boolean {
        val continuesGroup = content is NodeContent.Header && (previousWasHeader || imageOrSeparatorAfterHeader)
        imageOrSeparatorAfterHeader =
            (content is NodeContent.Image || content is NodeContent.Separator) && previousWasHeader
        previousWasHeader = content is NodeContent.Header
        return continuesGroup
    }
}
