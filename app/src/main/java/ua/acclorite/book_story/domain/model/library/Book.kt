/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.model.library

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
) : Parcelable {
    companion object {
        val default = Book(
            id = -1,
            title = "",
            author = UIText.StringValue(""),
            description = null,
            filePath = "",
            coverImage = null,
            scrollIndex = 0,
            scrollOffset = 0,
            progress = 0f,
            lastOpened = null,
            categories = emptyList()
        )
    }
}