/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.model.book

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.core.ui.UIText
import java.util.UUID

@Parcelize
@Immutable
data class Book(
    val id: Int,
    val uuid: String,

    val title: String,
    val author: UIText,
    val description: String?,

    val filePath: String,
    val coverImage: Uri?,

    val progress: BookProgress,

    val lastOpened: Long?,
    val categories: List<Int>,

    // Obsolete variables, will be deleted
    // at some point
    val scrollIndex: Int = -1,
    val scrollOffset: Int = -1,
    val oldProgress: Float = -1f
) : Parcelable {
    constructor(
        title: String,
        author: UIText,
        description: String?,
        filePath: String,
        coverImage: Uri?,
        progress: BookProgress,
        lastOpened: Long?,
        categories: List<Int>
    ) : this(
        id = 0,
        uuid = UUID.randomUUID().toString(),
        title = title,
        author = author,
        description = description,
        filePath = filePath,
        coverImage = coverImage,
        progress = progress,
        lastOpened = lastOpened,
        categories = categories
    )

    companion object {
        val default = Book(
            id = 0,
            uuid = "",
            title = "",
            author = UIText.StringValue(""),
            description = null,
            filePath = "",
            coverImage = null,
            progress = BookProgress.default,
            lastOpened = null,
            categories = emptyList()
        )
    }
}

/**
 * @property currentNodeIntraOffset Offset inside the node (to not always scroll to the start of the node only). Measured in chars for text.
 * @property currentOffset Current absolute offset (node offset + intra node offset).
 * @property totalLength Total length (offset). Used for percentage calculation.
 */
@Immutable
@Parcelize
data class BookProgress(
    val currentNodeId: NodeId,
    val currentNodeIntraOffset: Int,
    val totalNodes: Int,
    val currentOffset: Int,
    val totalLength: Int
) : Parcelable {
    fun getPercentage(): Float =
        if (totalLength <= 0) 0f else (currentOffset / totalLength.toFloat()).coerceIn(0f, 1f)

    companion object {
        val default = BookProgress(
            currentNodeId = NodeId(0),
            currentNodeIntraOffset = 0,
            totalNodes = 0,
            currentOffset = 0,
            totalLength = 0
        )
    }
}
