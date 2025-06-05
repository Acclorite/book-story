/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.model.file

import androidx.compose.runtime.Immutable

@Immutable
data class CachedFileBuilder(
    val name: String?,
    val path: String?,
    val size: Long? = null,
    val lastModified: Long? = null,
    val isDirectory: Boolean? = null
)