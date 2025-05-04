/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.browse.model

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class SelectableFile(
    val name: String,
    val uri: Uri,
    val path: String,
    val size: Long,
    val lastModified: Long,
    val isDirectory: Boolean,
    val selected: Boolean
)