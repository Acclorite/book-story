/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.language

import java.util.Locale

object LanguageUtils {
    fun findLanguage(
        languages: List<Language>,
        locale: Locale,
        defaultLanguage: Language
    ): Language {
        // Searching for exact locale match
        languages.find { language ->
            language.locale == locale
        }?.let { return it }

        // Searching for localLe language match
        languages.find { language ->
            language.locale.language == locale.language
        }?.let { return it }

        return defaultLanguage
    }
}