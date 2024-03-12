package ua.acclorite.book_story.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import ua.acclorite.book_story.util.UIText
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
    val coverImage: ImageBitmap?
)
