package ua.acclorite.book_story.domain.reader

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