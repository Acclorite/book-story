package ua.acclorite.book_story.domain.library.book

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.domain.library.category.Category
import ua.acclorite.book_story.domain.reader.Chapter
import ua.acclorite.book_story.domain.ui.UIText

@Parcelize
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
    val chapters: List<Chapter> = emptyList(),

    val lastOpened: Long?,
    val category: Category,
) : Parcelable