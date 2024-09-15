package ua.acclorite.book_story.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class Chapter(
    val index: Int = 0,
    val title: String,
    val startIndex: Int,
    val endIndex: Int
) : Parcelable

@Immutable
data class ChapterWithText(
    val chapter: Chapter,
    val text: List<String>
)