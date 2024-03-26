package ua.acclorite.book_story.domain.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.util.UIText
import java.io.File

@Immutable
data class Book(
    val id: Int = 0,

    val title: String,
    val author: UIText,
    val description: String?,

    val textPath: String,
    val text: List<StringWithId> = emptyList(),
    val letters: Int,
    val words: Int,

    val scrollIndex: Int,
    val scrollOffset: Int,
    val progress: Float,

    val file: File?,
    val filePath: String,
    val lastOpened: Long?,
    val category: Category,
    val coverImage: Uri?
)
