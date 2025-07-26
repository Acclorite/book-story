/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.model

import androidx.compose.runtime.Immutable
import java.util.Locale

@Immutable
@JvmInline
value class LanguageCode(val code: String) {
    companion object {
        fun getDisplayName(code: String): String {
            val target = Locale.forLanguageTag(code)
            val displayName = target.getDisplayName(target)
            return displayName.replaceFirstChar { it.uppercaseChar() }
        }
    }
}