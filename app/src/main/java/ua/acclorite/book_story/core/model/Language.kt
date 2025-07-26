/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class Language(
    val languageCode: LanguageCode,
    val displayName: String
) {
    companion object {
        fun fromCode(code: String, displayName: String = code): Language {
            return Language(
                languageCode = LanguageCode(code = code),
                displayName = LanguageCode.getDisplayName(code = displayName)
            )
        }
    }
}