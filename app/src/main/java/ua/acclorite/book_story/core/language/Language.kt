/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.language

import androidx.compose.runtime.Immutable
import java.util.Locale

@Immutable
data class Language(
    val locale: Locale,
    val displayName: String
) {
    companion object {
        fun getDisplayName(locale: Locale): String {
            return locale
                .getDisplayName(locale)
                .replaceFirstChar { it.uppercaseChar() }
        }

        fun fromLanguageTag(languageTag: String): Language {
            val locale = Locale.forLanguageTag(languageTag)
            return Language(
                locale = locale,
                displayName = getDisplayName(locale)
            )
        }
    }
}