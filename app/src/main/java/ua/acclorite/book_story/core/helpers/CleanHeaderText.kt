/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.helpers

private val headerMarkup = Regex("<[^>]*>")
private val headerMarkers = Regex("\\{\\{.*?\\}\\}", RegexOption.DOT_MATCHES_ALL)

fun String.cleanHeaderText(): String = replace(headerMarkup, "")
    .replace(headerMarkers, "")
    .replace("{{", "")
    .replace("}}", "")
    .replace(Regex("[\\t\\r\\n ]+"), " ")
    .trim()
