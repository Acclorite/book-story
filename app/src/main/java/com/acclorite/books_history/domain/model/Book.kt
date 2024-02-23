package com.acclorite.books_history.domain.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.acclorite.books_history.util.UIText
import java.io.File

@Immutable
data class Book(
    val id: Int?,
    val title: String,
    val author: UIText,
    val description: String?,
    val text: List<StringWithId> = emptyList(),
    val progress: Float,
    val file: File?,
    val filePath: String,
    val lastOpened: Long?,
    val category: Category,
    val coverImage: Bitmap?
)
