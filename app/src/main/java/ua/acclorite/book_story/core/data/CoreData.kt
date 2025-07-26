/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.data

import kotlinx.collections.immutable.toPersistentList
import ua.acclorite.book_story.core.model.Language

object CoreData {
    val defaultLanguage = Language.fromCode(code = "en")
    val languages = listOf(
        Language.fromCode(code = "en"),
        Language.fromCode(code = "uk"),
        Language.fromCode(code = "de"),
        Language.fromCode(code = "ar"),
        Language.fromCode(code = "es"),
        Language.fromCode(code = "tr"),
        Language.fromCode(code = "fr"),
        Language.fromCode(code = "pl"),
        Language.fromCode(code = "it"),
        Language.fromCode(code = "zh"),
        Language.fromCode(code = "hi"),
        Language.fromCode(code = "pt", displayName = "pt-BR"),
        Language.fromCode(code = "ta"),
        Language.fromCode(code = "ml"),
        Language.fromCode(code = "cs"),
        Language.fromCode(code = "ro"),
        Language.fromCode(code = "be"),
        Language.fromCode(code = "ja"),
    ).sortedBy { it.displayName }.toPersistentList()
}