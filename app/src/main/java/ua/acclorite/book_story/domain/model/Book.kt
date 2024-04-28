package ua.acclorite.book_story.domain.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.util.UIText

@Immutable
data class Book(
    val id: Int = 0,

    val title: String,
    val author: UIText,
    val description: String?,

    val textPath: String,
    val filePath: String,
    val coverImage: Uri?,

    val scrollIndex: Int,
    val scrollOffset: Int,
    val progress: Float,

    val lastOpened: Long?,
    val category: Category,
)
