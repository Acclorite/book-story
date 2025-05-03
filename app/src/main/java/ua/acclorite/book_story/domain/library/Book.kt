/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.library

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.core.ui.UIText

@Parcelize
@Immutable
data class Book(
    val id: Int = 0,

    val title: String,
    val author: UIText,
    val description: String?,

    val filePath: String,
    val coverImage: Uri?,

    val scrollIndex: Int,
    val scrollOffset: Int,
    val progress: Float,

    val lastOpened: Long?,
    val categories: List<Int>
) : Parcelable