/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.data

import kotlinx.collections.immutable.toPersistentList
import ua.acclorite.book_story.core.language.Language

object CoreData {
    val defaultLanguage = Language.fromLanguageTag(languageTag = "en") // English
    val languages = listOf(
        Language.fromLanguageTag(languageTag = "en"), // English
        Language.fromLanguageTag(languageTag = "uk"), // Ukrainian
        Language.fromLanguageTag(languageTag = "de"), // German
        Language.fromLanguageTag(languageTag = "ar"), // Arabic
        Language.fromLanguageTag(languageTag = "es"), // Spanish
        Language.fromLanguageTag(languageTag = "tr"), // Turkish
        Language.fromLanguageTag(languageTag = "fr"), // French
        Language.fromLanguageTag(languageTag = "pl"), // Polish
        Language.fromLanguageTag(languageTag = "it"), // Italian
        Language.fromLanguageTag(languageTag = "zh-CN"), // Chinese (China)
        Language.fromLanguageTag(languageTag = "hi"), // Hindi
        Language.fromLanguageTag(languageTag = "pt-BR"), // Portuguese (Brazil)
        Language.fromLanguageTag(languageTag = "ta"), // Tamil
        Language.fromLanguageTag(languageTag = "ml"), // Malayalam
        Language.fromLanguageTag(languageTag = "cs"), // Czech
        Language.fromLanguageTag(languageTag = "ro"), // Romanian
        Language.fromLanguageTag(languageTag = "be"), // Belarusian
        Language.fromLanguageTag(languageTag = "ja"), // Japanese
        Language.fromLanguageTag(languageTag = "hu"), // Hungarian
        Language.fromLanguageTag(languageTag = "ko"), // Korean
    ).sortedBy { it.displayName }.toPersistentList()
}