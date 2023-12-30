package com.acclorite.books_history.domain.model

import android.graphics.Bitmap
import java.io.File

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val description: String?,
    val text: List<FormattedLine> = emptyList(),
    val progress: Float,
    val file: File?,
    val lastOpened: Long?,
    val category: Category,
    val coverImage: Bitmap?
)
